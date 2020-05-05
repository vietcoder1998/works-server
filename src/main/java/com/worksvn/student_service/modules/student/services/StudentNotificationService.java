package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.requests.UserNotificationFilter;
import com.worksvn.common.modules.student.responses.StudentNotificationDto;
import com.worksvn.common.services.notification.NotificationService;
import com.worksvn.common.services.notification.models.NotificationAction;
import com.worksvn.common.services.notification.models.UserNotification;
import com.worksvn.common.utils.core.DateTimeUtils;
import com.worksvn.common.utils.jpa.JPAQueryBuilder;
import com.worksvn.common.utils.jpa.JPAQueryExecutor;
import com.worksvn.student_service.constants.NumberConstants;
import com.worksvn.student_service.modules.student.models.entities.StudentNotification;
import com.worksvn.student_service.modules.student.repositories.StudentNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StudentNotificationService {
    @Autowired
    private StudentNotificationRepository studentNotificationRepository;
    @Autowired
    private JPAQueryExecutor queryExecutor;

    @Autowired
    private NotificationService notificationService;

    public PageDto<StudentNotificationDto> queryStudentNotifications(String studentID,
                                                                     List<String> sortBy, List<String> sortType,
                                                                     int pageIndex, int pageSize,
                                                                     UserNotificationFilter filter) {
        if (filter == null) {
            filter = new UserNotificationFilter();
        }

        JPAQueryBuilder<StudentNotificationDto> queryBuilder = new JPAQueryBuilder<>();
        queryBuilder.selectAsObject(StudentNotificationDto.class,
                "sn.id", "sn.type", "sn.title", "sn.body",
                "sn.data", "sn.seen", "sn.createdDate")
                .from(StudentNotification.class, "sn");

        JPAQueryBuilder<StudentNotificationDto>.Condition whereCondition = queryBuilder.newCondition();
        whereCondition.and().paramCondition("sn.studentID", "=", studentID);

        if (filter.getSeen() != null) {
            whereCondition.and().paramCondition("sn.seen", "=", filter.getSeen());
        }

        if (filter.getCreatedDate() != null && filter.getCreatedDate() > 0) {
            Date createdDate = DateTimeUtils.extractDateOnly(filter.getCreatedDate());
            whereCondition.and().paramCondition("sn.createdDate", "=", createdDate);
        }

        if (filter.getNotificationType() != null) {
            whereCondition.and().paramCondition("sn.type", "=", filter.getNotificationType());
        }

        queryBuilder.where(whereCondition)
                .orderBy(sortBy, sortType)
                .setPagination(pageIndex, pageSize, NumberConstants.MAX_PAGE_SIZE);

        return queryExecutor.executePaginationQuery(queryBuilder);
    }

    public void seenStudentNotification(String studentID, String notificationID,
                                        boolean isSeen) {
        studentNotificationRepository.seenStudentNotification(studentID, notificationID, isSeen);
    }

    public void publishNewStudentNotification(UserNotification notification,
                                              NotificationAction overrideAction) {
        if (overrideAction != null) {
            notification.setAction(overrideAction);
        }
        notificationService.publishNotification(notification);
    }

    public void createNewStudentNotification(StudentNotification notification) {
        studentNotificationRepository.save(notification);
    }
}
