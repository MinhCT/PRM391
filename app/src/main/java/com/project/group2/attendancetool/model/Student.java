package com.project.group2.attendancetool.model;

/**
 * Created by User on 3/6/2018.
 */

public class Student {
    private String StudentId;
    private String FullName;
    private String Email;
    private String Image;

    public Student() {
    }

    public Student(String studentId, String fullName, String email, String image) {
        StudentId = studentId;
        FullName = fullName;
        Email = email;
        Image = image;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
