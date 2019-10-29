package com.worksvn.student_service.modules.school.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.responses.BranchDto;
import com.worksvn.common.modules.common.responses.MajorDto;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolEducationService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<BranchDto> getSchoolBranches(String schoolID,
                                                List<String> sortBy, List<String> sortType,
                                                int pageIndex, int pageSize) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .SCHOOL_getSchoolBranches(schoolID, sortBy, sortType, pageIndex, pageSize));
    }

    public PageDto<MajorDto> getSchoolMajors(String schoolID, Integer branchID,
                                             List<String> sortBy, List<String> sortType,
                                             int pageIndex, int pageSize) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .SCHOOL_getSchoolMajors(schoolID, branchID, sortBy, sortType, pageIndex, pageSize));
    }
}
