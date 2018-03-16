package com.project.group2.attendancetool.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.interfaces.IVolleyCallback;
import com.project.group2.attendancetool.model.UserInfo;
import com.project.group2.attendancetool.activity.student.StudentMainActivity;
import com.project.group2.attendancetool.activity.teacher.TeacherMainActivity;
import com.project.group2.attendancetool.enums.ELogTag;
import com.project.group2.attendancetool.enums.ERequestCode;
import com.project.group2.attendancetool.request.LoginManagement;
import com.project.group2.attendancetool.utils.Constants;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Login Activity, launcher activity - Perform Login functions
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
        // Check for existing logged in account, update UI accordingly
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
        setGoogleLoginButtonText("Sign In With FPT Mail");
    }

    /**
     * Call google sign in to retrieve basic profile after a successful login
     */
    private void setupSignInClient() {
        // Configure sign-in to request the user's ID, email address, and basic profile.
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
        if (isUserInfoNotStored()) {
            try {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                String idToken = account.getIdToken();
                LoginManagement loginManagement = new LoginManagement(this);
                loginManagement.validateLoginToken(new IVolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        UserInfo userInfo = new Gson().fromJson(result, UserInfo.class);
                        storeUserInfo(userInfo);
                        updateUI(userInfo.getUserRole());
                    }
                }, idToken); // Validate id token and start corresponding activity based on retrieved user info
            } catch (ApiException e) {
                Log.w(ELogTag.LOGIN_ERROR.getTagDescription(), "Failed to login, code: " + e.getStatusCode());
                updateUI(null);
            }
        } else {
            updateUI(getUserRoleFromCache());
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
            subscribeFirebaseNotifier();
            Intent teacherIntent = new Intent(this, TeacherMainActivity.class);
            startActivity(teacherIntent);
        } else if (loginRole.equalsIgnoreCase("student")) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("teacher_AnhBN");
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent studentIntent = new Intent(this, StudentMainActivity.class);
            startActivity(studentIntent);
        }
    }

    /**
     * Check if user info has been stored locally or not
     *
     * @return - boolean indicating stored status
     */
    private boolean isUserInfoNotStored() {
        SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userId = userInfoPreferences.getString(Constants.UserInfoSharedPreferenceKey.USER_ID_KEY, null);
        String userFullName = userInfoPreferences.getString(Constants.UserInfoSharedPreferenceKey.USER_FULLNAME_KEY, null);
        String userEmail = userInfoPreferences.getString(Constants.UserInfoSharedPreferenceKey.USER_FULLNAME_KEY, null);
        String userRole = userInfoPreferences.getString(Constants.UserInfoSharedPreferenceKey.USER_ROLE_KEY, null);

        return userId == null && userEmail == null && userRole == null && userFullName == null;
    }

    /**
     * Store user info in app local storage
     *
     * @param userInfo - info of logged in user to store
     */
    private void storeUserInfo(UserInfo userInfo) {
        SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = userInfoPreferences.edit();
        editor.putString(Constants.UserInfoSharedPreferenceKey.USER_ID_KEY, userInfo.getUserId());
        editor.putString(Constants.UserInfoSharedPreferenceKey.USER_FULLNAME_KEY, userInfo.getFullName());
        editor.putString(Constants.UserInfoSharedPreferenceKey.USER_EMAIL_KEY, userInfo.getUserEmail());
        editor.putString(Constants.UserInfoSharedPreferenceKey.USER_ROLE_KEY, userInfo.getUserRole());
        editor.apply();
    }

    /**
     * Find from cache and return logged in role name of the user
     *
     * @return - current login role name of the user
     */
    private String getUserRoleFromCache() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString(Constants.UserInfoSharedPreferenceKey.USER_ROLE_KEY, null);
    }

    /**
     * Subscribe a listener to listen for student attendance report through Firebase
     */
    private void subscribeFirebaseNotifier() {
        SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseMessaging.getInstance().subscribeToTopic("teacher_"
                + userInfoPreferences.getString(Constants.UserInfoSharedPreferenceKey.USER_ID_KEY, ""));
    }

    /**
     * Set text for Google Login Button
     *
     * @param buttonText - text to set in the Google Login Button
     */
    private void setGoogleLoginButtonText(String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < btnLogin.getChildCount(); i++) {
            View v = btnLogin.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }
}
