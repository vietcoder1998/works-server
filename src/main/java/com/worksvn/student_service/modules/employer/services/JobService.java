package com.worksvn.student_service.modules.employer.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.employer.enums.JobHomePriority;
import com.worksvn.common.modules.employer.requests.ActiveJobFilter;
import com.worksvn.common.modules.employer.requests.HomeActiveJobFilter;
import com.worksvn.common.modules.employer.requests.SearchActiveJobFilter;
import com.worksvn.common.modules.employer.responses.JobDto;
import com.worksvn.common.modules.employer.responses.JobPreview;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<JobPreview> getHomeActiveJobs(List<String> sortBy, List<String> sortType,
                                                 Integer pageIndex, Integer pageSize,
                                                 HomeActiveJobFilter filter, JobHomePriority priority) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .EMPLOYER_getHomeActiveJobs(sortBy, sortType, pageIndex, pageSize, filter, priority));
    }

    public PageDto<JobPreview> searchActiveJobs(List<String> sortBy, List<String> sortType,
                                                int pageIndex, int pageSize,
                                                SearchActiveJobFilter filter) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .EMPLOYER_searchActiveJobs(sortBy, sortType, pageIndex, pageSize, filter));
    }

    public PageDto<JobPreview> getActiveJobs(List<String> sortBy, List<String> sortType,
                                             int pageIndex, int pageSize,
                                             ActiveJobFilter filter) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .EMPLOYER_queryActiveJobs(sortBy, sortType, pageIndex, pageSize, filter));
    }

    public JobDto getActiveJobDetail(String id, String userID, String userType,
                                     String schoolID, Boolean passSchoolIgnore,
                                     Double lat, Double lon) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .EMPLOYER_getActiveJobDetail(id, userID, userType, schoolID, passSchoolIgnore, lat, lon));
    }
}
