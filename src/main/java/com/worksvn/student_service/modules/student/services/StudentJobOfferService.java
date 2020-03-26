package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.enums.RequestState;
import com.worksvn.common.modules.common.responses.LatLon;
import com.worksvn.common.modules.employer.enums.JobOfferUserType;
import com.worksvn.common.modules.employer.requests.JobOfferRequestFilter;
import com.worksvn.common.modules.employer.requests.NewJobOfferRequestState;
import com.worksvn.common.modules.employer.responses.JobOfferRequestPreview;
import com.worksvn.common.modules.student.responses.StudentJobOfferInfo;
import com.worksvn.student_service.modules.employer.services.JobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentJobOfferService {
    @Autowired
    private JobOfferService jobOfferService;
    @Autowired
    private StudentService studentService;

    public PageDto<JobOfferRequestPreview> getStudentJobOfferPreviews(String studentID, RequestState state,
                                                                      List<String> sortBy, List<String> sortType,
                                                                      Integer pageIndex, Integer pageSize) throws Exception {
        LatLon location = studentService.getLatLon(studentID);
        if (location == null) {
            location = new LatLon();
        }
        JobOfferRequestFilter filter = new JobOfferRequestFilter();
        filter.setUserID(studentID);
        filter.setUserType(JobOfferUserType.STUDENT);
        filter.setCenterLat(location.getLat());
        filter.setCenterLon(location.getLon());
        filter.setState(state);
        return jobOfferService.getJobOfferPreviews(sortBy, sortType, pageIndex, pageSize, filter);
    }

    public void updateStudentJobOfferState(String studentID, String jobID,
                                           RequestState state) throws Exception {
        StudentJobOfferInfo offerInfo = studentService.getStudentOfferInfo(studentID);
        NewJobOfferRequestState newState = new NewJobOfferRequestState();
        newState.setUserID(studentID);
        newState.setUserType(JobOfferUserType.STUDENT);
        newState.setFirstName(offerInfo.getFirstName());
        newState.setLastName(offerInfo.getLastName());
        newState.setAvatarUrl(offerInfo.getAvatarUrl());
        jobOfferService.updateJobOfferState(jobID, state, newState);
    }
}
