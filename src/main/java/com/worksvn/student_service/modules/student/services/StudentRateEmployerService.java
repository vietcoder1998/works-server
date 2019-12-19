package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.employer.enums.EmployerRatingUserType;
import com.worksvn.common.modules.employer.requests.NewEmployerRatingDto;
import com.worksvn.common.modules.employer.requests.NewUserRateEmployerDto;
import com.worksvn.common.modules.employer.requests.UserRateEmployerInfo;
import com.worksvn.common.modules.employer.responses.EmployerAverageRatingDto;
import com.worksvn.common.modules.employer.responses.EmployerRatingDto;
import com.worksvn.common.modules.employer.responses.UserSimpleInfo;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentRateEmployerService {
    @Autowired
    private ISRestCommunicator restCommunicator;
    @Autowired
    private StudentService studentService;

    public EmployerAverageRatingDto rateEmployer(String studentID, String employerID,
                                                 NewUserRateEmployerDto newRating) throws Exception {
        NewEmployerRatingDto newEmployerRating = new NewEmployerRatingDto();
        BeanUtils.copyProperties(newRating, newEmployerRating);

        UserSimpleInfo info = studentService.getSimpleInfo(studentID);
        newEmployerRating.setUserInfo(new UserRateEmployerInfo(info, EmployerRatingUserType.STUDENT));

        return restCommunicator.exchangeForSuccess(APIs.EMPLOYER_rateEmployer(employerID, newEmployerRating));
    }

    public EmployerRatingDto getEmployerRating(String studentID, String employerID) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .EMPLOYER_getEmployerRating(employerID, studentID, EmployerRatingUserType.STUDENT));
    }
}
