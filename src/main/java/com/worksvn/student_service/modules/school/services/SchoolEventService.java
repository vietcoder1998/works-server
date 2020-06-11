package com.worksvn.student_service.modules.school.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.school.requests.SchoolEventManagementFilter;
import com.worksvn.common.modules.school.responses.SchoolEventDto;
import com.worksvn.common.modules.school.responses.SchoolEventPreviewDto;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolEventService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<SchoolEventPreviewDto> getSchoolEvents(SchoolEventManagementFilter filter,
                                                          List<String> sortBy, List<String> sortType,
                                                          Integer pageIndex, Integer pageSize) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .SCHOOL_querySchoolEvents(filter, sortBy, sortType, pageIndex, pageSize, false));
    }

    public SchoolEventDto getSchoolEvent(String schoolID, String eventID, Boolean activeCheck) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .SCHOOL_getSchoolEvent(schoolID, eventID, activeCheck, false));
    }
}
