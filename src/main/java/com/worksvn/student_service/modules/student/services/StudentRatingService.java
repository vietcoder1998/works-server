package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.common.enums.RequestState;
import com.worksvn.common.modules.employer.enums.JobApplyUserType;
import com.worksvn.common.modules.student.enums.StudentRatingUserType;
import com.worksvn.common.modules.student.requests.NewStudentRatingDto;
import com.worksvn.common.modules.student.requests.UserRateStudentInfo;
import com.worksvn.common.modules.student.responses.StudentAverageRatingDto;
import com.worksvn.common.modules.student.responses.StudentRatingDto;
import com.worksvn.common.modules.student.responses.UserRateStudentDto;
import com.worksvn.common.services.notification.NotificationFactory;
import com.worksvn.common.services.notification.NotificationService;
import com.worksvn.common.utils.jpa.JPAQueryBuilder;
import com.worksvn.common.utils.jpa.JPAQueryExecutor;
import com.worksvn.student_service.constants.NumberConstants;
import com.worksvn.student_service.modules.employer.services.JobApplyRequestService;
import com.worksvn.student_service.modules.student.models.entities.Student;
import com.worksvn.student_service.modules.student.models.entities.StudentRating;
import com.worksvn.student_service.modules.student.repositories.StudentRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentRatingService {
    @Autowired
    private StudentRatingRepository studentRatingRepository;
    @Autowired
    private JPAQueryExecutor queryExecutor;

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentAverageRatingService studentAverageRatingService;
    @Autowired
    private JobApplyRequestService jobApplyRequestService;
    @Autowired
    private NotificationService notificationService;

    public StudentAverageRatingDto rateStudent(String studentID,
                                               NewStudentRatingDto newRating) throws Exception {
        UserRateStudentInfo userInfo = newRating.getUserInfo();

        Student s = studentService.getStudent(studentID);

        if (!jobApplyRequestService.checkRequestStateOfEmployer(studentID, JobApplyUserType.STUDENT,
                userInfo.getId(), RequestState.ACCEPTED)) {
            throw new ResponseException(ResponseValue.NO_ACCEPTED_APPLIED_JOB);
        }

        StudentRating rating = studentRatingRepository
                .findFirstByStudent_IdAndUserIDAndUserType(studentID, userInfo.getId(), userInfo.getUserType());
        boolean isUpdate = true;
        if (rating == null) {
            isUpdate = false;
            rating = new StudentRating(s);
        }
        rating.update(newRating);

        notificationService.publishNotification(NotificationFactory
                .CAN_STU_newRated(s.getId(), userInfo.getId(), userInfo.getUserType().name(),
                        userInfo.getName(), userInfo.getAvatarUrl(), isUpdate));

        studentRatingRepository.save(rating);

        return studentAverageRatingService.getStudentAverageRatingDto(studentID);
    }

    public StudentRatingDto getStudentRatingDto(String studentID,
                                                String userID, StudentRatingUserType userType) throws ResponseException {
        StudentRatingDto ratingDto = studentRatingRepository.getStudentRatingDto(studentID, userID, userType);
        if (ratingDto == null) {
            throw new ResponseException(ResponseValue.RATING_NOT_FOUND);
        }
        return ratingDto;
    }

    public PageDto<UserRateStudentDto> getStudentRatings(String studentID,
                                                         List<String> sortBy, List<String> sortType,
                                                         int pageIndex, int pageSize) {
        JPAQueryBuilder<UserRateStudentDto> queryBuilder = new JPAQueryBuilder<>();
        queryBuilder.selectAsObject(UserRateStudentDto.class,
                "sr.attitudeRating", "sr.skillRating", "sr.jobAccomplishmentRating",
                "sr.comment", "sr.createdDate", "sr.lastModified",
                "sr.userID", "sr.userType", "sr.name", "sr.avatarUrl")
                .from(StudentRating.class, "sr")
                .where(queryBuilder.newCondition().paramCondition("sr.student.id", "=", studentID))
                .orderBy(sortBy, sortType)
                .setPagination(pageIndex, pageSize, NumberConstants.MAX_PAGE_SIZE);
        return queryExecutor.executePaginationQuery(queryBuilder);
    }
}
