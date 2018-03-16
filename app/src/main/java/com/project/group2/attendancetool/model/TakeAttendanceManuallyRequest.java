package com.project.group2.attendancetool.model;

import java.util.List;

/**
 * Created by User on 3/15/2018.
 */

public class TakeAttendanceManuallyRequest {
    private String UserId;
    private String RoleName;
    private int SlotId;
    private String Date;
    private List<StudentAttendance> Students;

    public TakeAttendanceManuallyRequest(String userId, String roleName, int slotId, String date, List<StudentAttendance> students) {
        UserId = userId;
        RoleName = roleName;
        SlotId = slotId;
        Date = date;
        Students = students;
    }

    public String getUserId() {
        return UserId;
    }

    public String getRoleName() {
        return RoleName;
    }

    public int getSlotId() {
        return SlotId;
    }

    public String getDate() {
        return Date;
    }

    public List<StudentAttendance> getStudents() {
        return Students;
    }
}
