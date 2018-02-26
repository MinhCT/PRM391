package com.project.group2.attendancetool.response;

import com.project.group2.attendancetool.model.Course;

import java.util.List;

/**
 * Response Model for list of courses
 */
public class CourseResponse {
    private List<Course> Courses;

    public List<Course> getCourses() {
        return Courses;
    }
}
