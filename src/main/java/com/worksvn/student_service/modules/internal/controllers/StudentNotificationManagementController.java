package com.worksvn.student_service.modules.internal.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.requests.UserNotificationFilter;
import com.worksvn.common.modules.student.responses.StudentNotificationDto;
import com.worksvn.common.services.notification.models.NotificationAction;
import com.worksvn.common.services.notification.models.UserNotification;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.models.entities.StudentNotification;
import com.worksvn.student_service.modules.student.services.StudentNotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AuthorizationRequired
@PreAuthorize("#oauth2.hasScope('INTERNAL_SERVICE')")
@Api(description = "Quản lý thông báo sinh viên")
@RequestMapping("/api/internal/students")
public class StudentNotificationManagementController extends BaseRESTController {
    @Autowired
    private StudentNotificationService studentNotificationService;

    @ApiOperation(value = "Xem danh sách")
    @Responses(value = {
    })
    @PostMapping("/{sid}/notifications/query")
    public PageDto<StudentNotificationDto> getStudentNotifications(
            @PathVariable("sid") String studentID,
            @RequestParam(value = "sortBy", defaultValue = StudentNotification.CREATED_DATE) List<String> sortBy,
            @RequestParam(value = "sortType", defaultValue = "desc") List<String> sortType,
            @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "0") Integer pageSize,
            @RequestBody(required = false) UserNotificationFilter filter) {
        return studentNotificationService.queryStudentNotifications(studentID,
                sortBy, sortType, pageIndex, pageSize, filter);
    }

    @ApiOperation(value = "Tạo mới")
    @Responses(value = {
    })
    @PostMapping("/notifications/publish")
    public void publishNewStudentNotification(@RequestParam(value = "overrideAction", required = false) NotificationAction overrideAction,
                                              @RequestBody UserNotification userNotification) {
        studentNotificationService.publishNewStudentNotification(userNotification, overrideAction);
    }
}
