package com.project.group2.attendancetool.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.group2.attendancetool.enums.ELogTag;
import com.project.group2.attendancetool.enums.EWebApiEndpoints;
import com.project.group2.attendancetool.helper.EncodeVolleyBody;
import com.project.group2.attendancetool.interfaces.IVolleyCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Class contains methods to manage notification data from web service after a successful api call
 */
public class NotificationManagement {
    private Context applicationContext;

    public NotificationManagement(Context context) {
        applicationContext = context;
    }

    /**
     * Get a list of terms from web service
     *
     * @param callback - callback target to send response content to
     */
    public void getNotifications(final IVolleyCallback callback) {
        final int[] statusCode = {0};
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest getRequest = new StringRequest(
                Request.Method.POST,
                EWebApiEndpoints.LOAD_NOTIFICATION_ENDPOINT.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (statusCode[0] == 200) {
                            // Pass the response to callback functions to handle after volley has
                            // finished the request and receive response
                            callback.onSuccess(response);
                        }
                    }
                },
                createErrorResponseListener()
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                params.put("UserId", userInfoPreferences.getString("userId", null));
                params.put("RoleName", userInfoPreferences.getString("userRole", null));
                return EncodeVolleyBody.encodeParams(params, "UTF-8");
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                statusCode[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        requestQueue.add(getRequest);
    }

    /**
     * Get the current context, used within this class only
     *
     * @return - the application context currently making Volley request
     */
    private Context getApplicationContext() {
        return applicationContext;
    }

    /**
     * Create a new Response.ErrorListener for Volley request
     *
     * @return - a Response.ErrorListener object
     */
    private Response.ErrorListener createErrorResponseListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ELogTag.VOLLEY_ERROR.toString(), error.toString());
            }
        };
    }
}
