package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.student.responses.UserRateStudentDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.models.entities.StudentRating;
import com.worksvn.student_service.modules.student.services.StudentRatingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AuthorizationRequired
@Api(description = "Đánh giá của nhà truyển dụng")
@RequestMapping("/api/students")
public class StudentRatingController extends BaseRESTController {
    @Autowired
    private StudentRatingService studentRatingService;

    @ApiOperation(value = "Xem dánh sách")
    @Responses(value = {
    })
    @GetMapping("/ratings")
    public PageDto<UserRateStudentDto> getStudentRatings(
            @RequestParam(value = "sortBy", defaultValue = "sr." + StudentRating.LAST_MODIFIED) List<String> sortBy,
            @RequestParam(value = "sortType", defaultValue = "desc") List<String> sortType,
            @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "0") int pageSize) {
        String studentID = getAuthorizedUser().getId();
        return studentRatingService.getStudentRatings(studentID, sortBy, sortType, pageIndex, pageSize);
    }
}
