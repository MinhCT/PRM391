package com.project.group2.attendancetool.response;

import com.project.group2.attendancetool.model.SlotInformation;
import com.project.group2.attendancetool.model.StudentAttendance;

import java.util.List;

/**
 * Created by User on 3/6/2018.
 */

public class GetSlotDetailResponse {
    private SlotInformation SlotInformation;
    private List<StudentAttendance> Students;

    public com.project.group2.attendancetool.model.SlotInformation getSlotInformation() {
        return SlotInformation;
    }

    public List<StudentAttendance> getStudents() {
        return Students;
    }
}
