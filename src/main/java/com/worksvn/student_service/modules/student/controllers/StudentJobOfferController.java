package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.enums.RequestState;
import com.worksvn.common.modules.employer.responses.JobOfferRequestPreview;
import com.worksvn.common.modules.employer.responses.SimpleJobOfferRequest;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentJobOfferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AuthorizationRequired
@Api(description = "Lời mời ứng tuyển")
@RequestMapping("/api/students/jobs")
public class StudentJobOfferController extends BaseRESTController {
    @Autowired
    private StudentJobOfferService studentJobOfferService;

    @ApiOperation(value = "Lấy danh sách lời mời ứng tuyển")
    @Responses({
    })
    @GetMapping("/offer")
    public PageDto<JobOfferRequestPreview> getJobOfferPreviews(
            @RequestParam(value = "sortBy", required = false) List<String> sortBy,
            @RequestParam(value = "sortType", required = false) List<String> sortType,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "state", required = false) RequestState state) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentJobOfferService.getStudentJobOfferPreviews(studentID, state, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Xem nội dung lời mời ứng tuyển (simple)")
    @Responses({
    })
    @GetMapping("/{id}/offer")
    public SimpleJobOfferRequest getSimpleJobOffer(@PathVariable("id") String jobID) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentJobOfferService.getSimpleJobOfferRequest(studentID, jobID);
    }

    @ApiOperation(value = "Từ chối lời mời ứng tuyển")
    @Responses({
    })
    @PostMapping("/{id}/offer/rejected")
    public void cancelJobOfferRequest(@PathVariable("id") String offerJobID) throws Exception {
        String studentID = getAuthorizedUser().getId();
        studentJobOfferService.updateStudentJobOfferState(studentID, offerJobID, RequestState.REJECTED);
    }
}
