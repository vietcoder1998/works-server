package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.employer.enums.JobHomePriority;
import com.worksvn.common.modules.employer.requests.ClientActiveJobFilter;
import com.worksvn.common.modules.employer.requests.ClientHomeActiveJobFilter;
import com.worksvn.common.modules.employer.requests.ClientSearchActiveJobFilter;
import com.worksvn.common.modules.employer.responses.JobDto;
import com.worksvn.common.modules.employer.responses.JobPreview;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentJobService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AuthorizationRequired
@Api(description = "Bài đăng đang hoạt động")
@RequestMapping("/api/students/jobs")
public class StudentJobController extends BaseRESTController {
    @Autowired
    private StudentJobService studentJobService;

    @ApiOperation(value = "Xem danh sách bài đăng ở trang chủ")
    @Responses(value = {
    })
    @PostMapping("/active/home")
    public PageDto<JobPreview> getHomeActiveJobs(@RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                                 @RequestParam(value = "sortType", required = false) List<String> sortType,
                                                 @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                 @RequestParam(value = "priority", required = false) JobHomePriority priority,
                                                 @RequestParam(value = "majorMatching", defaultValue = "false") boolean majorMatching,
                                                 @RequestParam(value = "schoolConnected", defaultValue = "false") boolean schoolConnected,
                                                 @RequestBody(required = false) ClientHomeActiveJobFilter filter) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentJobService.getStudentHomeActiveJobs(studentID,
                schoolConnected, majorMatching,
                filter, priority, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Tìm kiếm bài đăng ở trang chủ")
    @Responses(value = {
    })
    @PostMapping("/active/search")
    public PageDto<JobPreview> searchActiveJobs(@RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                                @RequestParam(value = "sortType", required = false) List<String> sortType,
                                                @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                                @RequestParam(value = "pageSize", defaultValue = "0") Integer pageSize,
                                                @RequestParam(value = "majorMatching", defaultValue = "false") boolean majorMatching,
                                                @RequestParam(value = "schoolConnected", defaultValue = "false") boolean schoolConnected,
                                                @RequestBody(required = false) ClientSearchActiveJobFilter filter) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentJobService.searchStudentActiveJobs(studentID,
                schoolConnected, majorMatching,
                filter, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Xem danh sách việc đang khả dụng")
    @Responses(value = {
    })
    @PostMapping("/active")
    public PageDto<JobPreview> getActiveJobs(@RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                             @RequestParam(value = "sortType", required = false) List<String> sortType,
                                             @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                             @RequestParam(value = "pageSize", defaultValue = "0") Integer pageSize,
                                             @RequestParam(value = "majorMatching", defaultValue = "false") boolean majorMatching,
                                             @RequestParam(value = "schoolConnected", defaultValue = "false") boolean schoolConnected,
                                             @RequestBody(required = false) ClientActiveJobFilter filter) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentJobService.getStudentActiveJobs(studentID,
                schoolConnected, majorMatching,
                filter, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Xem chi tiết (check active)")
    @Responses(value = {
            @Response(responseValue = ResponseValue.JOB_NOT_FOUND),
            @Response(responseValue = ResponseValue.JOB_HIDDEN),
            @Response(responseValue = ResponseValue.JOB_DISABLED)
    })
    @GetMapping("/{id}/active")
    public JobDto getActiveJobDetail(@PathVariable("id") String jobID,
                                     @RequestParam(value = "schoolEventID", required = false) String schoolEventID)
            throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentJobService.getStudentActiveJobDetail(studentID, jobID, schoolEventID);
    }

    @ApiOperation(value = "Xem chi tiết")
    @Responses(value = {
            @Response(responseValue = ResponseValue.JOB_NOT_FOUND),
            @Response(responseValue = ResponseValue.JOB_HIDDEN),
            @Response(responseValue = ResponseValue.JOB_DISABLED)
    })
    @GetMapping("/{id}")
    public JobDto getJobDetail(@PathVariable("id") String jobID,
                               @RequestParam(value = "schoolEventID", required = false) String schoolEventID)
            throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentJobService.getStudentJobDetail(studentID, jobID, schoolEventID);
    }
}
