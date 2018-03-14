package com.project.group2.attendancetool.response;

import com.project.group2.attendancetool.model.StudentAttendance;

import java.util.List;

/**
 * Created by User on 3/14/2018.
 */

public class TakeAttendanceByImageResponse {
    private List<StudentAttendance> Students;

    public TakeAttendanceByImageResponse(List<StudentAttendance> students) {
        Students = students;
    }

    public List<StudentAttendance> getStudents() {
        return Students;
    }

    public void setStudents(List<StudentAttendance> students) {
        Students = students;
    }
}

