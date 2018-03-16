package com.project.group2.attendancetool.request;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.group2.attendancetool.enums.ELogTag;
import com.project.group2.attendancetool.enums.EWebApiEndpoints;
import com.project.group2.attendancetool.interfaces.IVolleyCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class contains methods to manage login flows by calling to remote web service
 */
public class LoginManagement {

    private Context context;

    public LoginManagement(Context context) {
        this.context = context;
    }

    /**
     * Send request to back-end to verify the id token and then retrieve the User Info used to store
     * to application local storage
     *
     * @param callback - callback function to process response
     * @param idToken  - google account id token to validate
     */
    public void validateLoginToken(final IVolleyCallback callback, final String idToken) {
        final int[] responseCode = new int[1];

        // Build a request to check if the login token is valid
        RequestQueue requestQueue = Volley.newRequestQueue(getCurrentContext());
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

    public Context getCurrentContext() {
        return context;
    }
}
