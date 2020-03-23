package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.employer.requests.ClientActiveJobFilter;
import com.worksvn.common.modules.employer.responses.JobDto;
import com.worksvn.common.modules.employer.responses.JobPreview;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentActiveSchoolEventJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AuthorizationRequired
@Api(description = "Bài đăng trong sự kiện trường đang hoạt động")
@RequestMapping("/api/students/schools/events/{eid}")
public class StudentActiveSchoolEventJobController extends BaseRESTController {
    @Autowired
    private StudentActiveSchoolEventJobService studentActiveSchoolEventJobService;

    @ApiOperation(value = "Xem danh sách")
    @Responses(value = {
    })
    @PostMapping("/jobs/active")
    public PageDto<JobPreview> getActiveJobs(@PathVariable("eid") String eventID,
                                             @RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                             @RequestParam(value = "sortType", required = false) List<String> sortType,
                                             @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                             @RequestBody(required = false) ClientActiveJobFilter filter) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentActiveSchoolEventJobService.queryActiveEmployerSchoolEventJobs(studentID, eventID, filter,
                sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Xem chi tiết")
    @Responses(value = {
            @Response(responseValue = ResponseValue.JOB_NOT_FOUND),
            @Response(responseValue = ResponseValue.JOB_HIDDEN),
            @Response(responseValue = ResponseValue.JOB_DISABLED)
    })
    @GetMapping("/jobs/{jid}/active")
    public JobDto getJobDetail(@PathVariable("eid") String eventID,
                               @PathVariable("jid") String jobID) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentActiveSchoolEventJobService.getActiveEmployerSchoolEventJobDetail(studentID, eventID, jobID);
    }
}
