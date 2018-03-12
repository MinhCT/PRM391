package com.project.group2.attendancetool.model;

/**
 * Model class for storing data of teacher's notifications
 */
public class Notification {
    private long ScheduleId;
    private String ClassId;
    private String StudentId;
    private String CourseName;
    private int SlotId;
    private String Date;

    public Notification(long scheduleId, String classId, String studentId, String courseName, int slotId, String date) {
        ScheduleId = scheduleId;
        ClassId = classId;
        StudentId = studentId;
        CourseName = courseName;
        SlotId = slotId;
        Date = date;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public long getScheduleId() {
        return ScheduleId;
    }

    public void setScheduleId(long scheduleId) {
        ScheduleId = scheduleId;
    }

    public String getClassId() {
        return ClassId;
    }

    public void setClassId(String classId) {
        ClassId = classId;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public int getSlotId() {
        return SlotId;
    }

    public void setSlotId(int slotId) {
        SlotId = slotId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
