package org.city.alert.alert.service.service.imple;

import org.city.alert.alert.service.entity.Issue;
import org.city.alert.alert.service.entity.Notification;
import org.city.alert.alert.service.entity.User;
import org.city.alert.alert.service.repository.NotificationRepository;
import org.city.alert.alert.service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void createNotification(User user, String message, Issue issue) {
        Notification notification = Notification.builder()
                .userId(user.getId())
                .issueId(issue.getId())
                .message(message)
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);
        return notificationRepository.save(notification);
    }
}
