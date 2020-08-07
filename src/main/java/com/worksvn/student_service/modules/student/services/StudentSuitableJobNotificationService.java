package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.modules.employer.requests.ActiveJobCountFilter;
import com.worksvn.common.modules.employer.responses.JobCountDto;
import com.worksvn.common.modules.student.responses.StudentRegionMajorCountDto;
import com.worksvn.common.services.notification.NotificationFactory;
import com.worksvn.common.services.notification.NotificationService;
import com.worksvn.common.utils.core.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class StudentSuitableJobNotificationService {
    private static final Logger logger = LoggerFactory.getLogger(StudentSuitableJobNotificationService.class);

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentJobService studentJobService;
    @Autowired
    private NotificationService notificationService;

    public void publishStudentSuitableJobNotification(Date startCreatedDate, Date endCreatedDate) throws Exception {
        List<StudentRegionMajorCountDto> studentCounts = studentService.getStudentRegionMajorCount();
        for (StudentRegionMajorCountDto studentCount : studentCounts) {
            ActiveJobCountFilter filter = new ActiveJobCountFilter();
            filter.setRegionID(studentCount.getRegionID());
            filter.setMajorID(studentCount.getMajorID());
            filter.setStartCreatedDate(startCreatedDate.getTime());
            filter.setEndCreatedDate(endCreatedDate.getTime());
            JobCountDto jobCountResult = studentJobService.getStudentActiveJobsCount(filter);
            boolean skipped = jobCountResult.getJobCount() == 0;
            logger.info("[STUDENT SUITABLE JOB NOTIFICATION] Suitable Job Notification: " +
                            "createdDate={} majorID={} regionID={} studentCount={} jobFound={} ... {}",
                    DateTimeUtils.formatDate(startCreatedDate) + "-" + DateTimeUtils.formatDate(endCreatedDate),
                    studentCount.getMajorID(), studentCount.getRegionID(),
                    studentCount.getStudentCount(), jobCountResult.getJobCount(),
                    skipped? "SKIP PUSH" : "PUSH");
            if (!skipped) {
                Set<String> studentIDs = studentService
                        .getStudentIDsByRegionAndMajor(studentCount.getRegionID(), studentCount.getMajorID());
                if (!studentIDs.isEmpty()) {
                    for (String studentID : studentIDs) {
                        notificationService.publishNotification(NotificationFactory
                                .STUDENT_newSuitableJobs(studentID, jobCountResult.getJobCount(), startCreatedDate, endCreatedDate));
                    }
                }
            }
        }
    }
}
