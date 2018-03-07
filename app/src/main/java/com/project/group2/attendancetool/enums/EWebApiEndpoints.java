package com.project.group2.attendancetool.enums;

/**
 * Enum class contains all endpoints used to call Web API
 */
public enum EWebApiEndpoints {
    LOGIN_ENDPOINT("api/Login/GetUserInfoByIdToken"),
    LOAD_TERM_ENDPOINT("api/ScheduleManagement/GetTermByUser"),
    LOAD_COURSE_ENDPOINT("api/ScheduleManagement/GetCourseByTerm"),
    LOAD_STUDENT_SCHEDULE_ENDPOINT("api/ScheduleManagement/GetScheduleByUser"),
    REPORT_ATTENDANCE_ENDPOINT("api/AttendanceReport/ReportToTeacher");

    private final String HOST_URL = "http://10.0.2.2:58262/";
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
