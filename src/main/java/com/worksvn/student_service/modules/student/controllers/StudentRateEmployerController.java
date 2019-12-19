package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.modules.employer.requests.NewUserRateEmployerDto;
import com.worksvn.common.modules.employer.responses.EmployerAverageRatingDto;
import com.worksvn.common.modules.employer.responses.EmployerRatingDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentRateEmployerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AuthorizationRequired
@Api(description = "Đánh giá nhà tuyển dụng")
@RequestMapping("/api/students/employers")
public class StudentRateEmployerController extends BaseRESTController {
    @Autowired
    private StudentRateEmployerService studentRateEmployerService;

    @ApiOperation(value = "Xem đánh giá")
    @Responses(value = {
    })
    @GetMapping("/{id}/rating")
    public EmployerRatingDto getEmployerRating(@PathVariable("id") String employerID) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentRateEmployerService.getEmployerRating(studentID, employerID);
    }

    @ApiOperation(value = "Thêm/cập nhật đánh giá")
    @Responses(value = {
    })
    @PostMapping("/{id}/rating")
    public EmployerAverageRatingDto rateEmployer(@PathVariable("id") String employerID,
                                                 @RequestBody NewUserRateEmployerDto newRate) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentRateEmployerService.rateEmployer(studentID, employerID, newRate);
    }
}
