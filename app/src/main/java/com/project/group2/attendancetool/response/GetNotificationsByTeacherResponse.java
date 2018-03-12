package com.project.group2.attendancetool.response;

import com.project.group2.attendancetool.model.Notification;

import java.util.List;

/**
 * Response Model for list of notifications
 */
public class GetNotificationsByTeacherResponse {
    private List<Notification> Notifications;

    public List<Notification> getNotifications() {
        return Notifications;
    }
}
