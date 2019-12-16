package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.common.enums.RequestState;
import com.worksvn.common.modules.employer.responses.JobApplyRequestPreview;
import com.worksvn.common.modules.employer.responses.JobApplyResult;
import com.worksvn.common.modules.student.requests.NewStudentJobApplyRequestDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentJobApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AuthorizationRequired
@Api(description = "Ứng tuyển")
@RequestMapping("/api/students/jobs")
public class StudentJobApplyController extends BaseRESTController {
    @Autowired
    private StudentJobApplyService studentJobApplyService;

    @ApiOperation(value = "Xem danh sách bài đăng đã ứng tuyển")
    @Responses({
    })
    @GetMapping("/apply")
    public PageDto<JobApplyRequestPreview> getJobApplyRequestPreviews(@RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                                                      @RequestParam(value = "sortType", required = false) List<String> sortType,
                                                                      @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                                                      @RequestParam(value = "pageSize", defaultValue = "0") int pageSize,
                                                                      @RequestParam(value = "state", required = false) RequestState state) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentJobApplyService.getStudentJobApplyRequestPreviews(studentID, state, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Ứng tuyển")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND)
    })
    @PostMapping("/{id}/apply")
    public JobApplyResult applyJob(@PathVariable("id") String jobID,
                                   @RequestBody @Valid NewStudentJobApplyRequestDto newApplyRequest) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentJobApplyService.applyJob(jobID, studentID, newApplyRequest);
    }

//    @ApiOperation(value = "Hủy ứng tuyển")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "Hủy ứng tuyển thành công", response = OkResponseModel.class),
//            @ApiResponse(code = 401, message = "Access token hết hạn", response = TokenExpirationResponseModel.class),
//            @ApiResponse(code = 403, message = "Hồ sơ đã bị từ chối", response = EmployerRejectedResponseModel.class),
//            @ApiResponse(code = 404, message = "Không tìm thấy công việc", response = NotFoundResponseModel.class),
//            @ApiResponse(code = 409, message = "Ứng tuyển đã được chấp nhận, không thể hủy", response = EmployerAcceptedResponseModel.class)
//    })
//    @DeleteMapping("/appliedJobs/{id}")
//    public BaseResponse cancelAppliedjob(@ApiParam(name = "id", value = "ID job đã ứng tuyển cần hủy ứng tuyển")
//                                     @PathVariable("id") String jobID) {
//        BaseResponse baseResponse;
//        try {
//            String studentID = getAuthenticatedstudentID();
//            baseResponse = StudentAppliedJobService.cancelAppliedJob(studentID, jobID);
//        } catch (Exception e) {
//            baseResponse = new InternalServerErrorResponse();
//        }
//        return baseResponse;
//    }
}
