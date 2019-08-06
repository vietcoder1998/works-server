package com.worksvn.student_service.modules.services.firebase.fcm;

import com.worksvn.student_service.utils.base.HttpPostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FirebaseCloudMessagingService {
    @Value("${application.firebase.fcm.api:https://fcm.googleapis.com/fcm/send}")
    private String firebaseFCMApi;
    @Value("${application.firebase.fcm.legacy-server-key}")
    public String firebaseLegacyServerKey;

    @Autowired
    private RestTemplate restTemplate;

    public TopicNotificationResponse sendTopicNotification(TopicNotificationDto topicNotificationDto) {
        return new HttpPostRequest(restTemplate)
                .withUrl(firebaseFCMApi)
                .addToHeader("Authorization", "key=" + firebaseLegacyServerKey)
                .setJsonBody(topicNotificationDto)
                .execute(TopicNotificationResponse.class);
    }
}
