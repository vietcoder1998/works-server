package com.worksvn.student_service.modules.school.services;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.school.responses.SchoolSurveyFormDto;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolSurveyFormService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public SchoolSurveyFormDto getSchoolSurveyForm(String schoolID) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.SCHOOL_getSchoolSurveyForm(schoolID));
    }
}
