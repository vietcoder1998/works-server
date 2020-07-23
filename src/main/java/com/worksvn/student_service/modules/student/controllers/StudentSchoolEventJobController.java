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
@Api(description = "Nhà tuyển dụng tham gia sự kiện trường")
@RequestMapping("/api/students/schools/events/{evid}/jobs")
public class StudentSchoolEventJobController extends BaseRESTController {
    @Autowired
    private StudentJobService studentJobService;

    @ApiOperation(value = "Xem danh sách bài đăng ở trang chủ")
    @Responses(value = {
    })
    @PostMapping("/active/home")
    public PageDto<JobPreview> getHomeActiveJobs(@PathVariable("evid") String schoolEventID,
                                                 @RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                                 @RequestParam(value = "sortType", required = false) List<String> sortType,
                                                 @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                 @RequestParam(value = "priority", required = false) JobHomePriority priority,
                                                 @RequestParam(value = "majorMatching", defaultValue = "false") boolean majorMatching,
                                                 @RequestBody(required = false) ClientHomeActiveJobFilter filter) throws Exception {
        String studentID = getAuthorizedUser().getId();
        if (filter == null) {
            filter = new ClientHomeActiveJobFilter();
        }
        filter.setSchoolEventID(schoolEventID);
        return studentJobService.getStudentHomeActiveJobs(studentID, majorMatching,
                filter, priority, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Tìm kiếm bài đăng ở trang chủ")
    @Responses(value = {
    })
    @PostMapping("/active/search")
    public PageDto<JobPreview> searchActiveJobs(@PathVariable("evid") String schoolEventID,
                                                @RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                                @RequestParam(value = "sortType", required = false) List<String> sortType,
                                                @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                                @RequestParam(value = "pageSize", defaultValue = "0") Integer pageSize,
                                                @RequestParam(value = "majorMatching", defaultValue = "false") boolean majorMatching,
                                                @RequestBody(required = false) ClientSearchActiveJobFilter filter) throws Exception {
        String studentID = getAuthorizedUser().getId();
        if (filter == null) {
            filter = new ClientSearchActiveJobFilter();
        }
        filter.setSchoolEventID(schoolEventID);
        return studentJobService.searchStudentActiveJobs(studentID, majorMatching,
                filter, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Xem danh sách việc đang khả dụng")
    @Responses(value = {
    })
    @PostMapping("/active")
    public PageDto<JobPreview> getActiveJobs(@PathVariable("evid") String schoolEventID,
                                             @RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                             @RequestParam(value = "sortType", required = false) List<String> sortType,
                                             @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                             @RequestParam(value = "pageSize", defaultValue = "0") Integer pageSize,
                                             @RequestParam(value = "majorMatching", defaultValue = "false") boolean majorMatching,
                                             @RequestBody(required = false) ClientActiveJobFilter filter) throws Exception {
        String studentID = getAuthorizedUser().getId();
        if (filter == null) {
            filter = new ClientActiveJobFilter();
        }
        filter.setSchoolEventID(schoolEventID);
        return studentJobService.getStudentActiveJobs(studentID, majorMatching,
                filter, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Xem chi tiết (check active)")
    @Responses(value = {
            @Response(responseValue = ResponseValue.JOB_NOT_FOUND),
            @Response(responseValue = ResponseValue.JOB_HIDDEN),
            @Response(responseValue = ResponseValue.JOB_DISABLED)
    })
    @GetMapping("/{id}/active")
    public JobDto getActiveJobDetail(@PathVariable("evid") String schoolEventID,
                                     @PathVariable("id") String jobID) throws Exception {
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
    public JobDto getJobDetail(@PathVariable("evid") String schoolEventID,
                               @PathVariable("id") String jobID) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentJobService.getStudentJobDetail(studentID, jobID, schoolEventID);
    }
}
