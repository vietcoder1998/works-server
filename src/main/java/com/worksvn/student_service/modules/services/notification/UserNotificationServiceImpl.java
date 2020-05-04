package com.worksvn.student_service.modules.services.notification;

import com.worksvn.common.services.notification.UserNotificationService;
import com.worksvn.common.services.notification.models.UserNotification;
import com.worksvn.student_service.modules.student.models.entities.StudentNotification;
import com.worksvn.student_service.modules.student.services.StudentNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationServiceImpl implements UserNotificationService {
    @Autowired
    private StudentNotificationService studentNotificationService;

    @Override
    public void saveUserNotification(UserNotification notification) {
        switch (notification.getGroup()) {
            case STUDENT: {
                studentNotificationService.publishNewStudentNotification(new StudentNotification(notification));
            }
            break;

            default: {
                break;
            }
        }
    }
}
