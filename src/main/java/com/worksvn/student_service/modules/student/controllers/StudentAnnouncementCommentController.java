package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.admin.requests.AnnouncementCommentFilter;
import com.worksvn.common.modules.admin.requests.ClientAnnouncementCommentDto;
import com.worksvn.common.modules.admin.responses.AnnouncementCommentDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.admin.services.AnnouncementCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AuthorizationRequired
@Api(description = "Nhận xét bài viết hệ thống cho ứng viên")
@RequestMapping("/api/students/announcements")
public class StudentAnnouncementCommentController extends BaseRESTController {
    @Autowired
    private AnnouncementCommentService announcementCommentService;

    @ApiOperation(value = "Truy vấn danh sách")
    @Responses(value = {
    })
    @PostMapping("/{id}/comments/query")
    public PageDto<AnnouncementCommentDto> getAnnouncementComments(
            @PathVariable("id") String announcementID,
            @RequestParam(value = "sortBy", required = false) List<String> sortBy,
            @RequestParam(value = "sortType", required = false) List<String> sortType,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestBody(required = false) AnnouncementCommentFilter filter) throws Exception {
        return announcementCommentService.getAnnouncementCommentDtos(announcementID, sortBy, sortType, pageIndex, pageSize, filter);
    }

    @ApiOperation(value = "Xem chi tiết")
    @Responses(value = {
    })
    @GetMapping("/{aid}/comments/{cid}")
    public AnnouncementCommentDto getAnnouncementComment(@PathVariable("aid") String announcementID,
                                                         @PathVariable("cid") int commentID) throws Exception {
        return announcementCommentService.getAnnouncementCommentDto(announcementID, commentID);
    }

    @ApiOperation(value = "Xem nhận xét của cá nhân")
    @Responses(value = {
    })
    @GetMapping("/{aid}/comments")
    public AnnouncementCommentDto getUserAnnouncementComment(@PathVariable("aid") String announcementID) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return announcementCommentService.getUserAnnouncementCommentDto(announcementID, studentID);
    }

    @ApiOperation(value = "Thêm mới/cập nhật")
    @Responses(value = {
    })
    @PostMapping("/{id}/comments")
    public void createNewAnnouncementComment(@PathVariable("id") String announcementID,
                                             @RequestBody ClientAnnouncementCommentDto newComment) throws Exception {
        String studentID = getAuthorizedUser().getId();
        announcementCommentService.createNewAnnouncementComment(announcementID, studentID, newComment);
    }

    @ApiOperation(value = "Xóa danh sách")
    @Responses(value = {
    })
    @DeleteMapping("/{id}/comments")
    public void deleteAnnouncementComments(@PathVariable("id") String announcementID,
                                           @RequestBody Set<Integer> ids) throws Exception {
        String studentID = getAuthorizedUser().getId();
        announcementCommentService.deleteAnnouncementComments(announcementID, studentID, ids);
    }
}
