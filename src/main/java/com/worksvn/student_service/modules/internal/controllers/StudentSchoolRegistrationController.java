package com.worksvn.student_service.modules.internal.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.student.requests.NewStudentRegistrationDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AuthorizationRequired
@PreAuthorize("#oauth2.hasScope('INTERNAL_SERVICE')")
@Api(description = "Đăng ký tài khoản sinh viên")
@RequestMapping("/api/internal/students")
public class StudentSchoolRegistrationController extends BaseRESTController {
    @Autowired
    private StudentRegistrationService studentRegistrationService;

    @ApiOperation(value = "Đăng ký tài khoản sinh viên")
    @Responses(value = {
            @Response(responseValue = ResponseValue.STUDENT_EXISTS)
    })
    @PostMapping("/registration")
    public void registerNewStudent(@RequestParam("schoolID") String schoolID,
                                   @RequestBody @Valid NewStudentRegistrationDto registrationDto,
                                   @RequestParam(value = "activated", defaultValue = "true") boolean activated) throws Exception {
        studentRegistrationService.registerNewStudent(schoolID, registrationDto, activated);
    }
}
