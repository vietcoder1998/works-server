package com.worksvn.student_service.modules.admin.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.admin.enums.AnnouncementCommentTarget;
import com.worksvn.common.modules.admin.enums.AnnouncementTarget;
import com.worksvn.common.modules.admin.requests.AnnouncementCommentFilter;
import com.worksvn.common.modules.admin.requests.ClientAnnouncementCommentDto;
import com.worksvn.common.modules.admin.requests.NewAnnouncementCommentDto;
import com.worksvn.common.modules.admin.responses.AnnouncementCommentDto;
import com.worksvn.common.modules.employer.responses.UserSimpleInfo;
import com.worksvn.student_service.components.communication.APIs;
import com.worksvn.student_service.modules.student.services.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AnnouncementCommentService {
    @Autowired
    private ISRestCommunicator restCommunicator;
    @Autowired
    private StudentService studentService;

    public PageDto<AnnouncementCommentDto> getAnnouncementCommentDtos(String announcementID,
                                                                      List<String> sortBy, List<String> sortType,
                                                                      Integer pageIndex, Integer pageSize,
                                                                      AnnouncementCommentFilter filter) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.ADMIN_getAnnouncementComments(announcementID,
                AnnouncementTarget.STUDENT,
                sortBy, sortType, pageIndex, pageSize, filter));
    }

    public AnnouncementCommentDto getAnnouncementCommentDto(String announcementID, int commentID) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .ADMIN_getAnnouncementComment(announcementID, commentID, AnnouncementTarget.STUDENT));
    }

    public AnnouncementCommentDto getUserAnnouncementCommentDto(String announcementID, String userID) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .ADMIN_getUserAnnouncementComment(announcementID, AnnouncementTarget.STUDENT,
                        userID, AnnouncementCommentTarget.STUDENT));
    }

    public void createNewAnnouncementComment(String announcementID, String userID,
                                             ClientAnnouncementCommentDto comment) throws Exception {
        UserSimpleInfo info = studentService.getSimpleInfo(userID);
        NewAnnouncementCommentDto newComment = new NewAnnouncementCommentDto();
        BeanUtils.copyProperties(comment, newComment);
        newComment.setUserID(info.getId());
        newComment.setAvatarUrl(info.getAvatarUrl());
        newComment.setName(info.getFullName());
        newComment.setUserType(AnnouncementCommentTarget.STUDENT);
        restCommunicator.exchangeForSuccess(APIs
                .ADMIN_createNewAnnouncementComment(announcementID, AnnouncementTarget.STUDENT, newComment));
    }

    public void deleteAnnouncementComments(String announcementID, String userID,
                                           Set<Integer> ids) throws Exception {
        restCommunicator.exchangeForSuccess(APIs
                .ADMIN_deleteAnnouncementComments(announcementID, AnnouncementTarget.STUDENT,
                        userID, AnnouncementCommentTarget.STUDENT, ids));
    }
}
