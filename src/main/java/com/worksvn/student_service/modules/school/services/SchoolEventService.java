package com.worksvn.student_service.modules.school.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.school.responses.SchoolEventPreviewDto;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolEventService {
    @Autowired
    private ISRestCommunicator restCommunicator;

//    public PageDto<SchoolEventPreviewDto> getActiveSchoolEvents() {
//        return restCommunicator.exchangeForSuccess(APIs.)
//    }
}
