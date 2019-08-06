package com.worksvn.student_service.modules.services.firebase.fcm;

public class TopicNotificationDto {
    private static final String TOPIC_PREFIX = "/topics/";

    private String to;
    private Object data;

    public TopicNotificationDto() {
    }

    public TopicNotificationDto(String topicName, Object data) {
        this.to = TOPIC_PREFIX + topicName;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
