package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.responses.BranchDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.employer.services.SchoolEventJobBranchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/schools/events/{eid}/jobs")
@Api(description = "Nhóm nghành khả dụng trong sự kiện trường")
public class StudentSchoolEventJobBranchController extends BaseRESTController {
    @Autowired
    private SchoolEventJobBranchService schoolEventJobBranchService;

    @ApiOperation(value = "Xem chi tiết")
    @Responses(value = {
    })
    @GetMapping("/branches")
    public PageDto<BranchDto> getAvailableSchoolEventJobBranches(@PathVariable("eid") String eventID,
                                                                 @RequestParam(name = "sortBy", required = false) List<String> sortBy,
                                                                 @RequestParam(name = "sortType", required = false) List<String> sortType,
                                                                 @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
                                                                 @RequestParam(name = "pageSize", required = false) Integer pageSize) throws Exception {
        return schoolEventJobBranchService.getAvailableSchoolEventJobBranches(eventID, sortBy, sortType, pageIndex, pageSize);
    }
}
