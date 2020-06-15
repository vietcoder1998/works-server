package com.worksvn.student_service.modules.employer.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.employer.enums.JobHomePriority;
import com.worksvn.common.modules.employer.requests.*;
import com.worksvn.common.modules.employer.responses.JobDto;
import com.worksvn.common.modules.employer.responses.JobPreview;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolEventJobService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<JobPreview> getHomeSchoolEventActiveJobs(List<String> sortBy, List<String> sortType,
                                                            Integer pageIndex, Integer pageSize,
                                                            HomeActiveSchoolEventJobFilter filter, JobHomePriority priority) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .EMPLOYER_getHomeActiveSchoolEventJobs(sortBy, sortType, pageIndex, pageSize, filter, priority));
    }

    public PageDto<JobPreview> searchSchoolEventActiveJobs(List<String> sortBy, List<String> sortType,
                                                           Integer pageIndex, Integer pageSize,
                                                           SearchActiveSchoolEventJobFilter filter) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .EMPLOYER_searchActiveSchoolEventJobs(sortBy, sortType, pageIndex, pageSize, filter));
    }

    public PageDto<JobPreview> queryActiveEmployerSchoolEventJobs(ActiveSchoolEventJobFilter filter,
                                                                  List<String> sortBy, List<String> sortType,
                                                                  Integer pageIndex, Integer pageSize) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .SCHOOL_queryActiveEmployerSchoolEventJobs(filter, sortBy, sortType, pageIndex, pageSize));
    }

    public JobDto getActiveEmployerSchoolEventJobDetail(String schoolID, String eventID, String jobID,
                                                        String userID, String userType,
                                                        Double centerLat, Double centerLon) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .SCHOOL_getActiveEmployerSchoolEventJob(schoolID, eventID, jobID, userID, userType, centerLat, centerLon));
    }

    public JobDto getEmployerSchoolEventJobDetail(String schoolID, String eventID, String jobID,
                                                  String userID, String userType,
                                                  Double centerLat, Double centerLon) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .SCHOOL_getEmployerSchoolEventJob(schoolID, eventID, jobID, userID, userType, centerLat, centerLon));
    }
}
