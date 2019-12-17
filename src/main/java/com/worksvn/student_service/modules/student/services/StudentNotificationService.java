package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.student.responses.StudentNotificationDto;
import com.worksvn.common.utils.jpa.SortAndPageFactory;
import com.worksvn.student_service.constants.NumberConstants;
import com.worksvn.student_service.modules.student.models.entities.StudentNotification;
import com.worksvn.student_service.modules.student.repositories.StudentNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentNotificationService {
    @Autowired
    private StudentNotificationRepository studentNotificationRepository;

    public PageDto<StudentNotificationDto> getStudentNotifications(String studentID,
                                                                   List<String> sortBy, List<String> sortType,
                                                                   int pageIndex, int pageSize) {
        Pageable pageable = SortAndPageFactory
                .createPageable(sortBy, sortType, pageIndex, pageSize, NumberConstants.MAX_PAGE_SIZE);
        return new PageDto<>(studentNotificationRepository.getStudentNotificationDtos(studentID, pageable));
    }

    public void seenStudentNotification(String studentID, String notificationID,
                                          boolean isSeen) {
        studentNotificationRepository.seenStudentNotification(studentID, notificationID, isSeen);
    }

    public void createNewStudentNotification(StudentNotification notification) {
        studentNotificationRepository.save(notification);
    }
}
