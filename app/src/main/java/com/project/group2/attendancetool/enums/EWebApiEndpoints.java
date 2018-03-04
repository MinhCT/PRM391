package com.project.group2.attendancetool.enums;

/**
 * Enum class contains all endpoints used to call Web API
 */
public enum EWebApiEndpoints {
    LOGIN_ENDPOINT("api/login"),
    LOAD_TERM_ENDPOINT("api/term"),
    LOAD_COURSE_ENDPOINT("api/course"),
    LOAD_STUDENT_SCHEDULE_ENDPOINT("api/studentschedule"),
    REPORT_ATTENDANCE_ENDPOINT("api/report"),
    LOAD_SLOT_LIST_ENDPOINT("api/SlotManagement/GetSlotByTeacher");

    private final String HOST_URL = "http://10.0.2.2:29118/";
    private String fullEndpoint;

    EWebApiEndpoints(String url) {
        fullEndpoint = HOST_URL + url;
    }

    public String getFullEndpoint() {
        return fullEndpoint;
    }

    @Override
    public String toString() {
        return getFullEndpoint();
    }
}
