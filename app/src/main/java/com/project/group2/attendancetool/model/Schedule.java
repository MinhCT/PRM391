package com.project.group2.attendancetool.model;

/**
 * Model class for storing data of user's schedule
 */
public class Schedule {
    private long ScheduleId;
    private String Date;
    private int SlotNo;
    private String TeacherId;
    private String Room;
    private String ClassId;
    private String AttendanceStatus;
    private String ReportStatus;
    private String AttendanceSummary;

    public Schedule(long scheduleId, String day, int slotNo, String teacherId, String room, String classId, String attendanceStatus, String reportStatus, String attendanceSummary) {
        ScheduleId = scheduleId;
        Date = day;
        SlotNo = slotNo;
        TeacherId = teacherId;
        Room = room;
        ClassId = classId;
        AttendanceStatus = attendanceStatus;
        ReportStatus = reportStatus;
        AttendanceSummary = attendanceSummary;
    }

    public long getScheduleId() {
        return ScheduleId;
    }

    public String getDate() {
        return Date;
    }

    public int getSlotNo() {
        return SlotNo;
    }

    public String getTeacherId() {
        return TeacherId;
    }

    public String getRoom() {
        return Room;
    }

    public String getClassId() {
        return ClassId;
    }

    public String getAttendanceStatus() {
        return AttendanceStatus;
    }

    public String getReportStatus() {
        return ReportStatus;
    }

    public String getAttendanceSummary() {
        return AttendanceSummary;
    }
}
