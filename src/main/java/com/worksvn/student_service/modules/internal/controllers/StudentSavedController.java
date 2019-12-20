package com.worksvn.student_service.modules.internal.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.student.responses.StudentSavedPreview;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.models.entities.StudentSaved;
import com.worksvn.student_service.modules.student.services.StudentSavedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AuthorizationRequired
@PreAuthorize("#oauth2.hasScope('INTERNAL_SERVICE')")
@Api(description = "Lưu hồ sơ sinh viên")
@RequestMapping("/api/internal/students")
public class StudentSavedController extends BaseRESTController {
    @Autowired
    private StudentSavedService studentSavedService;

    @ApiOperation(value = "Xem danh sách")
    @Responses(value = {
    })
    @GetMapping("/saved")
    public PageDto<StudentSavedPreview> getStudentSaves(@RequestParam(value = "sortBy", defaultValue = "ss." + StudentSaved.CREATED_DATE) List<String> sortBy,
                                                        @RequestParam(value = "sortType", defaultValue = "desc") List<String> sortType,
                                                        @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                                        @RequestParam(value = "pageSize", defaultValue = "0") int pageSize,
                                                        @RequestParam(value = "employerID") String employerID) throws Exception {
        return studentSavedService.getStudentSaves(employerID, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Lưu hồ sơ")
    @Responses(value = {
            @Response(responseValue = ResponseValue.STUDENT_SAVED),
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND),
    })
    @PostMapping("/{id}/saved")
    public void saveStudent(@PathVariable("id") String studentID,
                            @RequestParam(value = "employerID") String employerID) throws Exception {
        studentSavedService.saveStudent(employerID, studentID);
    }

    @ApiOperation(value = "Xóa danh sách các ứng viên đã lưu")
    @Responses(value = {
    })
    @DeleteMapping("/saved")
    public void deleteStudentSaves(@RequestParam(value = "employerID") String employerID,
                                   @RequestBody Set<String> studentIDs) {
        studentSavedService.deleteStudentSaves(employerID, studentIDs);
    }
}
