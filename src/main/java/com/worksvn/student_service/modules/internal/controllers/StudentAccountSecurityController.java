package com.worksvn.student_service.modules.internal.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.auth.requests.ResetPasswordDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentAccountSecurityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AuthorizationRequired
@PreAuthorize("#oauth2.hasScope('INTERNAL_SERVICE')")
@Api(description = "Bảo mật tài khoản sinh viên")
@RequestMapping("/api/internal/students")
public class StudentAccountSecurityController extends BaseRESTController {
    @Autowired
    private StudentAccountSecurityService studentAccountSecurityService;

    @ApiOperation(value = "Đặt lại mật khẩu",
            notes = "[AUTH][internal][InternalUserAccountSecurityController] Đặt lại mật khẩu")
    @Responses(value = {
            @Response(responseValue = ResponseValue.SCHOOL_STUDENT_NOT_FOUND)
    })
    @PutMapping("/{id}/account/password/reset")
    public void resetStudentAccountPassword(@RequestParam("schoolID") String schoolID,
                                            @PathVariable("id") String studentID,
                                            @RequestBody @Valid ResetPasswordDto resetPasswordDto) throws Exception {
        studentAccountSecurityService.resetStudentAccountPassword(schoolID, studentID, resetPasswordDto);
    }
}
