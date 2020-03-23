package com.worksvn.student_service.modules.school.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.employer.requests.ActiveJobFilter;
import com.worksvn.common.modules.employer.responses.JobDto;
import com.worksvn.common.modules.employer.responses.JobPreview;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActiveEmployerSchoolEventJobService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<JobPreview> queryActiveEmployerSchoolEventJobs(String schoolID, String eventID,
                                                                ActiveJobFilter filter,
                                                                List<String> sortBy, List<String> sortType,
                                                                Integer pageIndex, Integer pageSize) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .SCHOOL_queryActiveEmployerSchoolEventJobs(schoolID, eventID,
                        filter, sortBy, sortType, pageIndex, pageSize));
    }

    public JobDto getActiveEmployerSchoolEventJobDetail(String schoolID, String eventID, String jobID,
                                                        String userID, String userType,
                                                        Double centerLat, Double centerLon) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .SCHOOL_getActiveEmployerSchoolEventJob(schoolID, eventID, jobID, userID, userType, centerLat, centerLon));
    }
}
