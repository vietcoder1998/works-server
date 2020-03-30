package com.worksvn.student_service.modules.employer.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.school.requests.SchoolEventEmployerFilter;
import com.worksvn.common.modules.school.responses.SchoolEventEmployerDto;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolEventEmployerService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<SchoolEventEmployerDto> querySchoolEventEmployers(String eventID, String schoolID,
                                                                     SchoolEventEmployerFilter filter,
                                                                     List<String> sortBy, List<String> sortType,
                                                                     Integer pageIndex, Integer pageSize) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.SCHOOL_querySchoolEventEmployers(eventID, schoolID, filter,
                sortBy, sortType, pageIndex, pageSize));
    }

    public SchoolEventEmployerDto getSchoolEventEmployer(String schoolID, String eventID, String employerID) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.SCHOOL_getSchoolEventEmployer(schoolID, eventID, employerID));
    }
}
