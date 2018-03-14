package com.project.group2.attendancetool.helper;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper class to convert an object to json object and reverse
 */
public class JsonObjectConverter<T> {
    public JSONObject toJsonObject(T t) {
        Gson gson = new Gson();
        try {
            return new JSONObject(gson.toJson(t, t.getClass()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
