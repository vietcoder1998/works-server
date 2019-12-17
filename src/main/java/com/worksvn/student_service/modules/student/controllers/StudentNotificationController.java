package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.student.responses.StudentNotificationDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.models.entities.StudentNotification;
import com.worksvn.student_service.modules.student.services.StudentNotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AuthorizationRequired
@Api(description = "Thông báo của ứng viên")
@RequestMapping("/api/students/notifications")
public class StudentNotificationController extends BaseRESTController {
    @Autowired
    private StudentNotificationService studentNotificationService;

    @ApiOperation(value = "Xem danh sách")
    @Responses(value = {
    })
    @GetMapping
    public PageDto<StudentNotificationDto> getStudentNotifications(
            @RequestParam(value = "sortBy", defaultValue = StudentNotification.CREATED_DATE) List<String> sortBy,
            @RequestParam(value = "sortType", defaultValue = "desc") List<String> sortType,
            @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "0") Integer pageSize) {
        String studentID = getAuthorizedUser().getId();
        return studentNotificationService.getStudentNotifications(studentID, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Xem/bỏ xem")
    @Responses(value = {
    })
    @PutMapping("/{id}/seen/{state}")
    public void seenStudentNotification(@PathVariable("id") String notificationID,
                                          @PathVariable("state") boolean isSeen) {
        String studentID = getAuthorizedUser().getId();
        studentNotificationService.seenStudentNotification(studentID, notificationID, isSeen);
    }
}
