package com.project.group2.attendancetool.interfaces;

import org.json.JSONObject;

/**
 * Interface allows to create custom callback whenever Volley call is used
 */
public interface IVolleyCallback {
    void onSuccess(String result);
}
