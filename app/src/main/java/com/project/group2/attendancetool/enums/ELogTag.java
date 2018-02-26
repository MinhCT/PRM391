package com.project.group2.attendancetool.enums;

/**
 * List out Log Tag names for the whole application
 */
public enum ELogTag {
    LOGIN_ERROR("GoogleLoginError"),
    JSON_PARSING_ERROR("ErrorParsingJson"),
    VOLLEY_ERROR("VolleyError");

    private String tagDescription;

    ELogTag(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    @Override
    public String toString() {
        return getTagDescription();
    }
}
