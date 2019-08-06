package com.worksvn.student_service.modules.services.firebase.fcm.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.worksvn.student_service.modules.services.firebase.fcm.NewFirebaseTopicNotificationEvent;
import com.worksvn.student_service.modules.services.firebase.fcm.TopicNotificationResponse;
import com.worksvn.student_service.utils.base.HttpPostRequest;
import com.worksvn.student_service.utils.base.JacksonObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FirebaseNotificationEventListener implements ApplicationListener<NewFirebaseTopicNotificationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseNotificationEventListener.class);

    @Value("${application.firebase.fcm.api:https://fcm.googleapis.com/fcm/send}")
    private String firebaseFCMApi;
    @Value("${application.firebase.fcm.legacy-server-key}")
    public String firebaseLegacyServerKey;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void onApplicationEvent(NewFirebaseTopicNotificationEvent event) {
        try {
            TopicNotificationResponse response = new HttpPostRequest(restTemplate)
                    .withUrl(firebaseFCMApi)
                    .addToHeader("Authorization", "key=" + firebaseLegacyServerKey)
                    .setJsonBody(event.getTopicNotification())
                    .execute(TopicNotificationResponse.class);
            try {
                logger.info("[FCM][CREATED] {}", JacksonObjectMapper.getInstance().writeValueAsString(response));
            } catch (JsonProcessingException ignored) {
            }
        } catch (Exception e) {
            logger.error("FirebaseCloudMessagingEventListener", e);
        }
    }
}
