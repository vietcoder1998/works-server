package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.school.requests.SchoolEventEmployerFilter;
import com.worksvn.common.modules.school.responses.SchoolEventEmployerDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentSchoolEventEmployerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AuthorizationRequired
@Api(description = "Nhà tuyển dụng tham gia sự kiện trường")
@RequestMapping("/api/students/schools/events/{evid}")
public class StudentSchoolEventEmployerController extends BaseRESTController {
    @Autowired
    private StudentSchoolEventEmployerService studentSchoolEventEmployerService;

    @ApiOperation(value = "Truy vấn danh sách")
    @Responses(value = {
    })
    @PostMapping("/employers/query")
    public PageDto<SchoolEventEmployerDto> getStudentSchoolEventPreviews(
            @PathVariable("eid") String eventID,
            @RequestParam(name = "sortBy", defaultValue = "see.priority") List<String> sortBy,
            @RequestParam(name = "sortType", defaultValue = "desc") List<String> sortType,
            @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "0") int pageSize,
            @RequestBody(required = false) SchoolEventEmployerFilter filter) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentSchoolEventEmployerService.querySchoolEventEmployers(studentID, eventID, filter,
                sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Xem chi tiết")
    @Responses(value = {
    })
    @GetMapping("/employers/{emid}")
    public SchoolEventEmployerDto getSchoolEventEmployer(@PathVariable("evid") String eventID,
                                                         @PathVariable("emid") String employerID) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentSchoolEventEmployerService.getSchoolEventEmployer(studentID, eventID, employerID);
    }
}
