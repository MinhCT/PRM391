package com.project.group2.attendancetool.model;

/**
 * Created by User on 3/6/2018.
 */

public class StudentAttendance extends Student {
    private String AttendanceStatus;

    public StudentAttendance(String studentId, String fullName, String email, String image, String attendanceStatus) {
        super(studentId, fullName, email, image);
        AttendanceStatus = attendanceStatus;
    }

    public String getAttendanceStatus() {
        return AttendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        AttendanceStatus = attendanceStatus;
    }
}
