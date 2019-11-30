package com.worksvn.student_service.modules.admin.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.admin.enums.AnnouncementTarget;
import com.worksvn.common.modules.admin.responses.AnnouncementTypeDto;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementTypeService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<AnnouncementTypeDto> getAnnouncementTypeDtos(List<String> sortBy, List<String> sortType,
                                                                int pageIndex, int pageSize,
                                                                AnnouncementTarget target) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.ADMIN_getAnnouncementTypes(sortBy, sortType, pageIndex, pageSize, target));
    }
}
