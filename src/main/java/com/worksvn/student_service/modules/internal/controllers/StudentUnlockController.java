package com.worksvn.student_service.modules.internal.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.student.responses.StudentContactInfo;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentUnlockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AuthorizationRequired
@PreAuthorize("#oauth2.hasScope('INTERNAL_SERVICE')")
@Api(description = "Mở khóa hồ sơ")
@RequestMapping("/api/internal/students")
public class StudentUnlockController extends BaseRESTController {
    @Autowired
    private StudentUnlockService studentUnlockService;

    @ApiOperation(value = "Mở khóa hồ sơ")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_UNLOCKED)
    })
    @PutMapping("/{id}/unlocked")
    public StudentContactInfo unlockProfile(@PathVariable("id") String studentID,
                                            @RequestParam(value = "employerID") String employerID) throws Exception {
        return studentUnlockService.unlockStudentByEmployer(studentID, employerID);
    }
}
