package org.city.alert.alert.service.service;

import org.city.alert.alert.service.entity.Issue;
import org.city.alert.alert.service.entity.Notification;
import org.city.alert.alert.service.entity.User;

import java.util.List;

public interface NotificationService {

    // Create a new notification for a user about an issue
    void createNotification(User user, String message, Issue issue);

    // Retrieve all notifications for a user
    List<Notification> getNotificationsForUser(Long userId);

    // Mark a notification as read
    Notification markAsRead(Long notificationId);
}
