package com.project.group2.attendancetool.model;

/**
 * Model class for storing data of teacher's notifications
 */
public class Notification {
    private long ScheduleId;
    private Classes Classes;
    private String StudentId;
    private Course Course;
    private Slot Slot;
    private String Date;

    public Notification(long scheduleId, com.project.group2.attendancetool.model.Classes classes, String studentId, com.project.group2.attendancetool.model.Course course, com.project.group2.attendancetool.model.Slot slot, String date) {
        ScheduleId = scheduleId;
        Classes = classes;
        StudentId = studentId;
        Course = course;
        Slot = slot;
        Date = date;
    }

    public long getScheduleId() {
        return ScheduleId;
    }

    public void setScheduleId(long scheduleId) {
        ScheduleId = scheduleId;
    }

    public com.project.group2.attendancetool.model.Classes getClasses() {
        return Classes;
    }

    public void setClasses(com.project.group2.attendancetool.model.Classes classes) {
        Classes = classes;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public com.project.group2.attendancetool.model.Course getCourse() {
        return Course;
    }

    public void setCourse(com.project.group2.attendancetool.model.Course course) {
        Course = course;
    }

    public com.project.group2.attendancetool.model.Slot getSlot() {
        return Slot;
    }

    public void setSlot(com.project.group2.attendancetool.model.Slot slot) {
        Slot = slot;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}

