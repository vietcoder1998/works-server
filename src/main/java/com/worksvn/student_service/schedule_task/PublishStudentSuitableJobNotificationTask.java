package com.worksvn.student_service.schedule_task;

import com.worksvn.common.utils.core.DateTimeUtils;
import com.worksvn.student_service.modules.student.services.StudentSuitableJobNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PublishStudentSuitableJobNotificationTask {
    private static final Logger logger = LoggerFactory.getLogger(PublishStudentSuitableJobNotificationTask.class);

    @Autowired
    private StudentSuitableJobNotificationService notificationService;

    @Value("${application.schedule-task.publish-student-suitable-job-notification.created-date.day.previous}")
    private int timeDelay;

//    @Scheduled(cron = "${application.schedule-task.publish-student-suitable-job-notification.cron.expression}")
//    public void scheduleFixedRateTaskAsync() throws Exception {
//        logger.info("[SCHEDULE TASK] Start publishing student suitable notification");
//        Date endDate = DateTimeUtils.addDayToDate(DateTimeUtils.extractDateOnly(new Date()), -1);
//        Date startDate = DateTimeUtils.addDayToDate(endDate, -timeDelay);
//        notificationService.publishStudentSuitableJobNotification(startDate, endDate);
//        logger.info("[SCHEDULE TASK] Stop publishing student suitable notification");
//    }
}
