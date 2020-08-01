package com.worksvn.student_service.modules.common.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.responses.MajorDto;
import com.worksvn.common.modules.common.services.BaseMajorService;
import com.worksvn.student_service.components.communication.APIs;
import com.worksvn.student_service.constants.CacheValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MajorService extends BaseMajorService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public PageDto<MajorDto> queryMajors(Integer branchID,
                                         String name, Boolean matchingName,
                                         Set<Integer> ids,
                                         List<String> sortBy, List<String> sortType,
                                         Integer pageIndex, Integer pageSize) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_queryMajors(branchID, name, matchingName, ids,
                sortBy, sortType, pageIndex, pageSize));
    }

    @Cacheable(cacheNames = CacheValue.MAJORS, key = "#id")
    @Override
    public MajorDto getMajorDto(int id) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_getMajor(id));
    }

    @Cacheable(cacheNames = CacheValue.MAJORS_BY_FTS, key = "#name", unless = "#result == null")
    public MajorDto findMajor(String name) throws Exception {
        PageDto<MajorDto> pageMajors = queryMajors(null, name, true,
                null, null, null, null, null);
        if (pageMajors != null && !pageMajors.getItems().isEmpty()) {
            return pageMajors.getItems().get(0);
        }
        return null;
    }
}
