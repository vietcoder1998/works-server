package com.worksvn.student_service.modules.school.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.responses.BranchDto;
import com.worksvn.common.modules.common.responses.MajorDto;
import com.worksvn.student_service.components.communication.APIs;
import com.worksvn.student_service.constants.CacheValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SchoolEducationService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<BranchDto> getSchoolBranches(String schoolID,
                                                List<String> sortBy, List<String> sortType,
                                                Integer pageIndex, Integer pageSize) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .SCHOOL_getSchoolBranches(schoolID, sortBy, sortType, pageIndex, pageSize));
    }

    public PageDto<MajorDto> getSchoolMajors(String schoolID, Integer branchID,
                                             String name, Boolean matchingName,
                                             List<String> sortBy, List<String> sortType,
                                             Integer pageIndex, Integer pageSize) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .SCHOOL_getSchoolMajors(schoolID, branchID, name, matchingName,
                        sortBy, sortType, pageIndex, pageSize));
    }

    @Cacheable(cacheNames = CacheValue.SCHOOL_MAJORS_BY_FTS, key = "{#schoolID, #name}")
    public MajorDto findSchoolMajor(String schoolID, String name) throws Exception {
        PageDto<MajorDto> pageMajors = getSchoolMajors(schoolID, null, name, true,
                null, null, null, null);
        if (pageMajors != null && !pageMajors.getItems().isEmpty()) {
            return pageMajors.getItems().get(0);
        }
        return null;
    }

    public void checkSchoolMajorExists(String schoolID, int majorID) throws Exception {
        restCommunicator.exchangeForSuccess(APIs.SCHOOL_checkSchoolMajorExists(schoolID, majorID));
    }

    public void addSchoolMajor(String schoolID, Set<Integer> majorIDs) throws Exception {
        restCommunicator.exchangeForSuccess(APIs.SCHOOL_addNewSchoolMajors(schoolID, majorIDs));
    }
}
