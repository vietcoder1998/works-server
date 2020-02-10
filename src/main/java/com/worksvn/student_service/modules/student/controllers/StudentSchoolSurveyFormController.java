package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.modules.school.responses.SchoolSurveyFormDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentSchoolSurveyFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AuthorizationRequired
@Api(description = "Phiếu khảo sát của trường")
@RequestMapping("/api/students/schools/surveys")
public class StudentSchoolSurveyFormController extends BaseRESTController {
    @Autowired
    private StudentSchoolSurveyFormService studentSchoolSurveyFormService;

    @ApiOperation(value = "Xem chi tiết")
    @Responses({
    })
    @GetMapping()
    public SchoolSurveyFormDto getStudentSchoolSurveyForm() throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentSchoolSurveyFormService.getStudentSchoolSurveyForm(studentID);
    }
}
