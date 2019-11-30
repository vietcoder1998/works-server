package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.admin.enums.AnnouncementTarget;
import com.worksvn.common.modules.admin.responses.AnnouncementTypeDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.admin.services.AnnouncementTypeService;
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
@Api(description = "Nhóm bài viết hệ thống cho ứng viên")
@RequestMapping("/api/students/announcementTypes")
public class StudentAnnouncementTypeController extends BaseRESTController {
    @Autowired
    private AnnouncementTypeService announcementTypeService;

    @ApiOperation(value = "Xem danh sách các nhóm bài viết")
    @Responses(value = {
    })
    @GetMapping()
    public PageDto<AnnouncementTypeDto> getAnnouncementTypes(@RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                                             @RequestParam(value = "sortType", required = false) List<String> sortType,
                                                             @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                                             @RequestParam(value = "pageSize", defaultValue = "0") Integer pageSize) throws Exception {
        return announcementTypeService.getAnnouncementTypeDtos(sortBy, sortType, pageIndex, pageSize, AnnouncementTarget.STUDENT);
    }
}
