package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.responses.WorkingToolDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentWorkingToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AuthorizationRequired
@Api(description = "Công cụ chuyên môn của ứng viên")
@RequestMapping("/api/students/workingTool")
public class StudentWorkingToolController extends BaseRESTController {
    @Autowired
    private StudentWorkingToolService studentWorkingToolService;

    @ApiOperation(value = "Xem danh sách")
    @Responses({
    })
    @GetMapping
    public PageDto<WorkingToolDto> getStudentWorkingTools(
            @RequestParam(value = "sortBy", required = false) List<String> sortBy,
            @RequestParam(value = "sortType", required = false) List<String> sortType) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentWorkingToolService.getStudentWorkingToolDtos(studentID, sortBy, sortType);
    }

    @ApiOperation(value = "Cập nhật danh sách")
    @Responses({
    })
    @PutMapping
    public void updateStudentWorkingTools(@RequestBody Set<Integer> workingToolIDs) throws Exception {
        String studentID = getAuthorizedUser().getId();
        studentWorkingToolService.updateStudentWorkingTools(studentID, workingToolIDs);
    }
}
