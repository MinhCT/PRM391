package com.project.group2.attendancetool.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.interfaces.IVolleyCallback;
import com.project.group2.attendancetool.model.UserInfo;
import com.project.group2.attendancetool.activity.student.StudentMainActivity;
import com.project.group2.attendancetool.activity.teacher.TeacherMainActivity;
import com.project.group2.attendancetool.enums.ELogTag;
import com.project.group2.attendancetool.enums.ERequestCode;
import com.project.group2.attendancetool.enums.EWebApiEndpoints;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Login Activity, launcher activity
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnLogin)
    SignInButton btnLogin;

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSignInClient();
        setupUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            googleSignInClient.silentSignIn()
                    .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
                        @Override
                        public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                            handleSignInResult(task);
                        }
                    });
        }
    }

    /**
     * Start the sign in intent and wait for retrieving result
     * Bind this method to sign in button
     */
    @OnClick(R.id.btnLogin)
    void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, ERequestCode.RC_SIGN_IN.getCode());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from method signIn
        if (requestCode == ERequestCode.RC_SIGN_IN.getCode()) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    /**
     * Setup UI controls bound with this activity
     */
    private void setupUI() {
        ButterKnife.bind(this);
    }

    private void setupSignInClient() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_login_server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    /**
     * Check local storage and perform sign in results base on the role of the user
     *
     * @param completedTask - the completed task after a intent result
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userId = userInfoPreferences.getString("userId", null);
        String userFullName = userInfoPreferences.getString("userFullName", null);
        String userEmail = userInfoPreferences.getString("userEmail", null);
        String userRole = userInfoPreferences.getString("userRole", null);

        // Check if user info has been stored in local storage or not
        if (userId == null && userEmail == null && userRole == null && userFullName == null) {
            try {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                String idToken = account.getIdToken();
                validateLoginToken(new IVolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        // Parse response from json to get required fields then store them in local storage
                        Gson gson = new Gson();
                        UserInfo userInfo = gson.fromJson(result, UserInfo.class);

                        SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = userInfoPreferences.edit();
                        editor.putString("userId", userInfo.getUserId());
                        editor.putString("userFullName", userInfo.getFullName());
                        editor.putString("userEmail", userInfo.getUserEmail());
                        editor.putString("userRole", userInfo.getUserRole());
                        editor.apply();

                        updateUI(userInfo.getUserRole());
                    }
                }, idToken); // Validate id token and start corresponding activity based on retrieved user info
            } catch (ApiException e) {
                // Log the error and notify in view
                Log.w(ELogTag.LOGIN_ERROR.getTagDescription(), "Failed to login, code: " + e.getStatusCode());
                updateUI(null);
            }
        } else {
            updateUI(userRole);
        }
    }

    /**
     * Check the role name and start the corresponding activity: Teacher or Student
     *
     * @param loginRole - role name of the verified user
     */
    private void updateUI(String loginRole) {
        if (loginRole == null) {
            Toast.makeText(this, "Failed to login, please check if your account's email is valid", Toast.LENGTH_SHORT).show();
        } else if (loginRole.equalsIgnoreCase("teacher")) {
            SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            FirebaseMessaging.getInstance().subscribeToTopic("teacher_" + userInfoPreferences.getString("userId", ""));
            Intent teacherIntent = new Intent(this, TeacherMainActivity.class);
            startActivity(teacherIntent);
        } else if (loginRole.equalsIgnoreCase("student")) {
            Intent studentIntent = new Intent(this, StudentMainActivity.class);
            startActivity(studentIntent);
        }
    }

    /**
     * Send request to back-end to verify the id token and then retrieve the User Info used to store
     * to application local storage
     *
     * @param callback - callback function to process response
     * @param idToken  - google account id token to validate
     */
    private void validateLoginToken(final IVolleyCallback callback, final String idToken) {
        final int[] responseCode = new int[1];

        // Build a request to check if the login token is valid
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, EWebApiEndpoints.LOGIN_ENDPOINT.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (responseCode[0] == 200) {
                            if (response != null) {
                                callback.onSuccess(response.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ELogTag.VOLLEY_ERROR.toString(), error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("idToken", idToken);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                responseCode[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        requestQueue.add(postRequest);
    }
}
