package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.school.responses.SchoolEventDto;
import com.worksvn.common.modules.school.responses.SchoolEventPreviewDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentSchoolEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AuthorizationRequired
@Api(description = "Sự kiện trường")
@RequestMapping("/api/students/schools/events")
public class StudentSchoolEventController extends BaseRESTController {
    @Autowired
    private StudentSchoolEventService studentSchoolEventService;

    @ApiOperation(value = "Truy vấn danh sách sự kiện đang hoạt động")
    @Responses(value = {
    })
    @GetMapping("/active")
    public PageDto<SchoolEventPreviewDto> getActiveStudentSchoolEvents(
            @RequestParam(name = "sortBy", required = false) List<String> sortBy,
            @RequestParam(name = "sortType", required = false) List<String> sortType,
            @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentSchoolEventService.getActiveStudentSchoolEvents(studentID, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Xem chi tiết sự kiện đang hoạt động")
    @Responses(value = {
    })
    @GetMapping("/{id}/active")
    public SchoolEventDto getActiveStudentSchoolEvent(@PathVariable("id") String id) throws Exception {
        String schoolID = getAuthorizedUser().getId();
        return studentSchoolEventService.getActiveStudentSchoolEvent(schoolID, id);
    }
}
