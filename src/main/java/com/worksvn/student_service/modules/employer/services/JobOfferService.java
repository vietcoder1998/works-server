package com.worksvn.student_service.modules.employer.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.enums.RequestState;
import com.worksvn.common.modules.employer.requests.JobOfferRequestFilter;
import com.worksvn.common.modules.employer.requests.NewJobOfferRequestState;
import com.worksvn.common.modules.employer.responses.JobOfferRequestPreview;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobOfferService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<JobOfferRequestPreview> getJobOfferPreviews(List<String> sortBy, List<String> sortType,
                                                               Integer pageIndex, Integer pageSize,
                                                               JobOfferRequestFilter filter) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .EMPLOYER_queryJobOfferRequestPreviews(sortBy, sortType, pageIndex, pageSize, filter));
    }

    public void updateJobOfferState(String jobID, RequestState state,
                                    NewJobOfferRequestState newState) throws Exception {
        restCommunicator.exchangeForSuccess(APIs.EMPLOYER_updateJobOfferState(jobID, state, newState));
    }
}
