package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.employer.responses.JobSavedPreview;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentJobSavedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AuthorizationRequired
@Api(description = "Bài đăng đã lưu")
@RequestMapping("/api/students/jobs")
public class StudentJobSaveController extends BaseRESTController {
    @Autowired
    private StudentJobSavedService studentJobSavedService;

    @ApiOperation(value = "Xem danh sách")
    @Responses({
    })
    @GetMapping("/saved")
    public PageDto<JobSavedPreview> getStudentSavedJobs(
            @RequestParam(value = "sortBy", required = false) List<String> sortBy,
            @RequestParam(value = "sortType", required = false) List<String> sortType,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentJobSavedService.getStudentJobSavedPreviews(studentID, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Lưu bài đăng")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND),
    })
    @PostMapping("/{id}/saved")
    public void saveJobs(@PathVariable("id") String jobID) throws Exception {
        String studentID = getAuthorizedUser().getId();
        studentJobSavedService.saveJob(jobID, studentID);
    }

    @ApiOperation(value = "Xóa danh sách bài đăng đã lưu")
    @Responses({
    })
    @DeleteMapping("/saved")
    public void deleteStudentSavedJobs(@RequestBody Set<String> jobIDs) throws Exception {
        String studentID = getAuthorizedUser().getId();
        studentJobSavedService.deleteJobSaves(studentID, jobIDs);
    }
}
