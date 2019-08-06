package com.worksvn.student_service.modules.services.firebase.fcm;

import org.springframework.context.ApplicationEvent;

public class NewFirebaseTopicNotificationEvent extends ApplicationEvent {
    private TopicNotificationDto topicNotification;

    public NewFirebaseTopicNotificationEvent(Object source, TopicNotificationDto topicNotification) {
        super(source);
        this.topicNotification = topicNotification;
    }

    public TopicNotificationDto getTopicNotification() {
        return topicNotification;
    }
}
