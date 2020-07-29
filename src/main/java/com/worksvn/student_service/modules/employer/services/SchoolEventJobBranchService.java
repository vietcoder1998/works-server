package com.worksvn.student_service.modules.employer.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.responses.BranchDto;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolEventJobBranchService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<BranchDto> getSchoolEventJobBranches(String eventID,
                                                        List<String> sortBy, List<String> sortType,
                                                        Integer pageIndex, Integer pageSize) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .EMPLOYER_getSchoolEventJobBranches(eventID, sortBy, sortType, pageIndex, pageSize));
    }
}
