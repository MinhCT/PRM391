package com.project.group2.attendancetool.enums;

/**
 * List out Log Tag names for the whole application
 */
public enum ELogTag {
    LOGIN_ERROR("GoogleLoginError"),
    JSON_PARSING_ERROR("ErrorParsingJson"),
    VOLLEY_ERROR("VolleyError"),
    SQLITE_ERROR("SQLiteDatabaseError"),
    PICASSO_IMAGE_LOADING_ERROR("ImageLoadError"),
    GET_IMAGE_BITMAT_FROM_IMAGEVIEW_ERROR("ImageBitmapError"),
    UI_COMPONENT_LOAD_ERROR("ComponentLoadError");

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
