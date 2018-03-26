package com.project.group2.attendancetool.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.group2.attendancetool.enums.ELogTag;
import com.project.group2.attendancetool.enums.EWebApiEndpoints;
import com.project.group2.attendancetool.interfaces.IVolleyJsonCallback;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by User on 3/6/2018.
 */

public class SlotManagement {

    private Context applicationContext;

    public SlotManagement(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    private Context getApplicationContext() {
        return applicationContext;
    }

    private Response.ErrorListener createErrorResponseListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ELogTag.VOLLEY_ERROR.toString(), error.toString());
            }
        };
    }

    public void getSlotDetail(final IVolleyJsonCallback callback, final String date, final int slotId, final String classId){
        final int[] statusCode = new int[1];
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        try{
            jsonObject.put("Date", date); //set for value date
            jsonObject.put("UserId", userInfoPreferences.getString("userId", null));//used in shared preference
            jsonObject.put("RoleName", userInfoPreferences.getString("userRole", null));//used in share preference
            jsonObject.put("SlotId", slotId);
            jsonObject.put("ClassId", classId);
            jsonArray.put(jsonObject);
        }catch(Exception e){
        }
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, EWebApiEndpoints.LOAD_SLOT_DETAIL_ENDPOINT.toString(), jsonObject,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        if (statusCode[0] == 200) {
                            // Pass the response to callback functions to handle after volley has
                            // finished the request and receive response
                            callback.onJsonRequestSucess(response);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:

                                    json = new String(response.data);
                                    System.out.println(json);
                                    break;
                            }
                            //Additional cases
                        }
                    }
                })
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                statusCode[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        // Send request to the server
        requestQueue.add(jsonObjectRequest);
    }
}
