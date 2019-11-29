package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.admin.enums.AnnouncementTarget;
import com.worksvn.common.modules.admin.requests.VisibleAnnouncementFilterDto;
import com.worksvn.common.modules.admin.responses.AnnouncementDto;
import com.worksvn.common.modules.admin.responses.AnnouncementPreview;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.admin.services.AnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AuthorizationRequired
@Api(description = "Bài viết hệ thống cho sinh viên")
@RequestMapping("/api/students/announcements")
public class StudentAnnouncementController extends BaseRESTController {
    @Autowired
    private AnnouncementService announcementService;

    @ApiOperation(value = "Xem danh sách các bài viết (visible)")
    @Responses(value = {
    })
    @PostMapping()
    public PageDto<AnnouncementPreview> getAnnouncements(@RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                                         @RequestParam(value = "sortType", required = false) List<String> sortType,
                                                         @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestBody(required = false) VisibleAnnouncementFilterDto filter) throws Exception {
        return announcementService.getAnnouncementPreviews(sortBy, sortType, pageIndex, pageSize, filter, AnnouncementTarget.STUDENT);
    }

    @ApiOperation(value = "Xem chi tiết nội dung bài viết (visible)")
    @Responses(value = {
    })
    @GetMapping("/{id}")
    public AnnouncementDto getAnnouncement(@PathVariable("id") String id) throws Exception {
        return announcementService.getAnnouncementDto(id, AnnouncementTarget.STUDENT);
    }
}
