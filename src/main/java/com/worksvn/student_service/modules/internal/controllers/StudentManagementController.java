package com.worksvn.student_service.modules.internal.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.student.requests.StudentFilterDto;
import com.worksvn.common.modules.student.responses.StudentPreview;
import com.worksvn.common.modules.student.responses.StudentProfileDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.models.entities.Student;
import com.worksvn.student_service.modules.student.services.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AuthorizationRequired
@PreAuthorize("#oauth2.hasScope('INTERNAL_SERVICE')")
@Api(description = "Quản lý sinh viên")
@RequestMapping("/api/internal/students")
public class StudentManagementController extends BaseRESTController {
    @Autowired
    private StudentService studentService;

    @ApiOperation(value = "Truy vấn danh sách")
    @Responses(value = {
    })
    @PostMapping("/query")
    public PageDto<StudentPreview> getStudentPreviews(@RequestParam(value = "sortBy", defaultValue = "s." + Student.FIRST_NAME + ",s." + Student.LAST_NAME) List<String> sortBy,
                                                      @RequestParam(value = "sortType", defaultValue = "asc,asc") List<String> sortType,
                                                      @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                                      @RequestParam(value = "pageSize", defaultValue = "0") int pageSize,
                                                      @RequestBody(required = false) StudentFilterDto filterDto) throws Exception {
        return studentService.getStudentPreviews(sortBy, sortType, pageIndex, pageSize, filterDto);
    }

    @ApiOperation(value = "Xem hồ sơ đầy đủ")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND),
            @Response(responseValue = ResponseValue.SCHOOL_STUDENT_NOT_FOUND),
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND),
    })
    @GetMapping("/{id}/profile")
    public StudentProfileDto getProfile(@PathVariable("id") String studentID,
                                        @RequestParam(value = "schoolID", required = false) String schoolID,
                                        @RequestParam(value = "employerID", required = false) String employerID) throws Exception {
        return studentService.getStudentProfile(studentID, schoolID, employerID);
    }

    @ApiOperation(value = "Kiểm tra tài khoản tồn tại")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND),
    })
    @GetMapping("/{id}/exists")
    public void checkStudentExists(@PathVariable("id") String studentID) throws ResponseException {
        studentService.checkStudentExist(studentID);
    }
}
