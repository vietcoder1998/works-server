package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.modules.school.responses.SchoolSurveyFormDto;
import com.worksvn.student_service.modules.school.services.SchoolSurveyFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentSchoolSurveyFormService {
    @Autowired
    private SchoolSurveyFormService schoolSurveyFormService;
    @Autowired
    private StudentService studentService;

    public SchoolSurveyFormDto getStudentSchoolSurveyForm(String studentID) throws Exception {
        String studentSchoolID = studentService.getStudentSchoolID(studentID);
        return schoolSurveyFormService.getSchoolSurveyForm(studentSchoolID);
    }
}
