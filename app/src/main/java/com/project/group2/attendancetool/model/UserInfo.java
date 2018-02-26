package com.project.group2.attendancetool.model;

/**
 * Model class containing user id, name, UserEmail and their login role (either Teacher or Student)
 */
public class UserInfo {
    private String UserId;
    private String FullName;
    private String UserEmail;
    private String UserRole;

    public UserInfo(String userId, String userFullName, String userEmail, String userRole) {
        UserId = userId;
        FullName = userFullName;
        UserEmail = userEmail;
        UserRole = userRole;
    }

    public String getFullName() {
        return FullName;
    }

    public String getUserId() {
        return UserId;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public String getUserRole() {
        return UserRole;
    }
}
