package com.worksvn.student_service.modules.employer.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.employer.enums.JobSavedUserType;
import com.worksvn.common.modules.employer.requests.JobSavedFilter;
import com.worksvn.common.modules.employer.responses.JobSavedPreview;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class JobSavedService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<JobSavedPreview> getJobSavePreviews(List<String> sortBy, List<String> sortType,
                                                       Integer pageIndex, Integer pageSize,
                                                       JobSavedFilter filter) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .EMPLOYER_queryJobSavedPreviews(sortBy, sortType, pageIndex, pageSize, filter));
    }

    public void saveJob(String userID, JobSavedUserType userType, String jobID) throws Exception {
        restCommunicator.exchangeForSuccess(APIs.EMPLOYER_saveJob(jobID, userID, userType));
    }

    public void deleteJobSaves(String userID, JobSavedUserType userType, Set<String> ids) throws Exception {
        restCommunicator.exchangeForSuccess(APIs.EMPLOYER_deleteJobSaves(ids, userID, userType));
    }
}
