package com.worksvn.student_service.modules.admin.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.admin.enums.AnnouncementTarget;
import com.worksvn.common.modules.admin.requests.VisibleAnnouncementFilterDto;
import com.worksvn.common.modules.admin.responses.AnnouncementDto;
import com.worksvn.common.modules.admin.responses.AnnouncementPreview;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<AnnouncementPreview> getAnnouncementPreviews(List<String> sortBy, List<String> sortType,
                                                                Integer pageIndex, Integer pageSize,
                                                                VisibleAnnouncementFilterDto filter,
                                                                AnnouncementTarget target) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .ADMIN_getAnnouncementPreviews(sortBy, sortType, pageIndex, pageSize, filter, target));
    }

    public AnnouncementDto getAnnouncementDto(String id, AnnouncementTarget target) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.ADMIN_getAnnouncementDto(id, target));
    }
}
