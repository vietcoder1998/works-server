package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.student.requests.SimpleStudentRegistrationDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AuthorizationRequired
@Api(description = "Đăng ký tài khoản sinh viên")
@RequestMapping("/api/students")
public class StudentRegistrationController extends BaseRESTController {
    @Autowired
    private StudentRegistrationService studentRegistrationService;

    @ApiOperation(value = "Đăng ký tài khoản sinh viên")
    @Responses(value = {
            @Response(responseValue = ResponseValue.STUDENT_EXISTS)
    })
    @PostMapping("/registration")
    public void registerNewStudent(@RequestParam("schoolID") String schoolID,
                                   @RequestBody @Valid SimpleStudentRegistrationDto registrationDto) throws Exception {
        studentRegistrationService.registerNewStudent(schoolID, registrationDto, false);
    }
}
