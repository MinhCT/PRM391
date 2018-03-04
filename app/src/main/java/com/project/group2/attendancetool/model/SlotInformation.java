package com.project.group2.attendancetool.model;

/**
 * Created by User on 3/4/2018.
 */

public class SlotInformation {
    public Slot Slot;
    public Course Course;
    public Classes Classes;

    public SlotInformation(Slot slot, Course course, Classes classes){
        Slot = slot;
        Course = course;
        Classes = classes;
    }

    public com.project.group2.attendancetool.model.Slot getSlot() {
        return Slot;
    }

    public void setSlot(com.project.group2.attendancetool.model.Slot slot) {
        Slot = slot;
    }

    public com.project.group2.attendancetool.model.Course getCourse() {
        return Course;
    }

    public void setCourse(com.project.group2.attendancetool.model.Course course) {
        Course = course;
    }

    public com.project.group2.attendancetool.model.Classes getClasses() {
        return Classes;
    }

    public void setClasses(com.project.group2.attendancetool.model.Classes classes) {
        Classes = classes;
    }
}
