package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.common.responses.BranchDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.employer.services.SchoolEventJobBranchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AuthorizationRequired
@Api(description = "Nghành nghề tuyển dụng trong sự kiện trường")
@RequestMapping("/api/students/schools/events/{evid}/jobs")
public class StudentSchoolEventJobBranchController extends BaseRESTController {
    @Autowired
    private SchoolEventJobBranchService schoolEventJobBranchService;

    @ApiOperation(value = "Xem nghành nghề tuyển dụng trong sự kiện")
    @Responses(value = {
            @Response(responseValue = ResponseValue.JOB_NOT_FOUND),
            @Response(responseValue = ResponseValue.JOB_HIDDEN),
            @Response(responseValue = ResponseValue.JOB_DISABLED)
    })
    @GetMapping("/branches")
    public PageDto<BranchDto> getSchoolEventJobBranches(@PathVariable("evid") String schoolEventID,
                                                        @RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                                        @RequestParam(value = "sortType", required = false) List<String> sortType,
                                                        @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize)
            throws Exception {
        return schoolEventJobBranchService.getSchoolEventJobBranches(schoolEventID, sortBy, sortType, pageIndex, pageSize);
    }
}
