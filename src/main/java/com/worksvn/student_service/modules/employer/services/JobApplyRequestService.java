package com.worksvn.student_service.modules.employer.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.enums.RequestState;
import com.worksvn.common.modules.employer.enums.JobApplyUserType;
import com.worksvn.common.modules.employer.requests.JobApplyRequestFilter;
import com.worksvn.common.modules.employer.requests.NewJobApplyRequestDto;
import com.worksvn.common.modules.employer.responses.JobApplyRequestPreview;
import com.worksvn.common.modules.employer.responses.JobApplyResult;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplyRequestService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<JobApplyRequestPreview> getJobApplyPreviews(List<String> sortBy, List<String> sortType,
                                                               int pageIndex, int pageSize,
                                                               JobApplyRequestFilter filter) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .EMPLOYER_queryJobApplyRequestPreviews(sortBy, sortType, pageIndex, pageSize, filter));
    }

    public JobApplyResult applyJob(String jobID, NewJobApplyRequestDto request) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.EMPLOYER_applyJob(jobID, request));
    }

    public boolean checkRequestStateOfEmployer(String userID, JobApplyUserType userType,
                                               String employerID, RequestState state) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.EMPLOYER_checkJobApplyState(userID, userType, employerID, state));
    }
}
