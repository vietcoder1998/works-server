package com.worksvn.student_service.modules.internal.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.student.enums.StudentRatingUserType;
import com.worksvn.common.modules.student.requests.NewStudentRatingDto;
import com.worksvn.common.modules.student.responses.StudentAverageRatingDto;
import com.worksvn.common.modules.student.responses.StudentRatingDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentRatingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AuthorizationRequired
@PreAuthorize("#oauth2.hasScope('INTERNAL_SERVICE')")
@Api(description = "Đánh giá sinh viên")
@RequestMapping("/api/internal/students")
public class RateStudentController extends BaseRESTController {
    @Autowired
    private StudentRatingService studentRatingService;

    @ApiOperation(value = "Xem đánh giá")
    @Responses(value = {
            @Response(responseValue = ResponseValue.RATING_NOT_FOUND)
    })
    @GetMapping("/{id}/rating")
    public StudentRatingDto getStudentRating(@PathVariable("id") String employerID,
                                             @RequestParam("userID") String userID,
                                             @RequestParam("userType") StudentRatingUserType userType) throws ResponseException {
        return studentRatingService.getStudentRatingDto(employerID, userID, userType);
    }

    @ApiOperation(value = "Thêm/cập nhật đánh giá")
    @Responses(value = {
            @Response(responseValue = ResponseValue.NO_ACCEPTED_APPLIED_JOB)
    })
    @PostMapping("/{id}/rating")
    public StudentAverageRatingDto rateStudent(@PathVariable("id") String employerID,
                                               @RequestBody @Valid NewStudentRatingDto newRating) throws Exception {
        return studentRatingService.rateStudent(employerID, newRating);
    }
}
