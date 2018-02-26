package com.project.group2.attendancetool.model;

/**
 * Model class representing by id and name
 */
public class Course {
    private String CourseId;
    private String CourseName;

    public Course(String courseId, String courseName) {
        CourseId = courseId;
        CourseName = courseName;
    }

    public String getCourseId() {
        return CourseId;
    }

    public String getCourseName() {
        return CourseName;
    }
}
