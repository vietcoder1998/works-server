package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.enums.RequestState;
import com.worksvn.common.modules.common.responses.LatLon;
import com.worksvn.common.modules.employer.enums.JobApplyUserType;
import com.worksvn.common.modules.employer.requests.JobApplyRequestFilter;
import com.worksvn.common.modules.employer.requests.NewJobApplyRequestDto;
import com.worksvn.common.modules.employer.responses.JobApplyRequestPreview;
import com.worksvn.common.modules.employer.responses.JobApplyResult;
import com.worksvn.common.modules.student.requests.NewStudentJobApplyRequestDto;
import com.worksvn.common.modules.student.responses.StudentJobApplyInfo;
import com.worksvn.student_service.modules.employer.services.JobApplyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentJobApplyService {
    @Autowired
    private JobApplyRequestService jobApplyRequestService;
    @Autowired
    private StudentService studentService;

    public PageDto<JobApplyRequestPreview> getStudentJobApplyRequestPreviews(String studentID, RequestState state,
                                                                             List<String> sortBy, List<String> sortType,
                                                                             int pageIndex, int pageSize) throws Exception {
        LatLon location = studentService.getLatLon(studentID);
        if (location == null) {
            location = new LatLon();
        }

        JobApplyRequestFilter filter = new JobApplyRequestFilter();
        filter.setUserID(studentID);
        filter.setUserType(JobApplyUserType.STUDENT);
        filter.setState(state);
        filter.setCenterLat(location.getLat());
        filter.setCenterLat(location.getLon());
        return jobApplyRequestService.getJobApplyPreviews(sortBy, sortType, pageIndex, pageSize, filter);
    }

    public JobApplyResult applyJob(String jobID, String studentID,
                                   NewStudentJobApplyRequestDto applyRequest) throws Exception {
        StudentJobApplyInfo applyInfo = studentService.getStudentApplyInfo(studentID);
        NewJobApplyRequestDto newRequest = new NewJobApplyRequestDto(studentID, JobApplyUserType.STUDENT,
                applyInfo.getFirstName(), applyInfo.getLastName(),
                applyInfo.getGender().name(), applyInfo.getAvatarUrl(),
                applyRequest.getMessage(), applyRequest.getShiftIDs());
        return jobApplyRequestService.applyJob(jobID, newRequest);
    }

    public boolean checkStudentAppliedStateOfEmployer(String studentID, String employerID, RequestState state) throws Exception {
        return jobApplyRequestService.checkRequestStateOfEmployer(studentID, JobApplyUserType.STUDENT,
                employerID, state);
    }
}
