package com.worksvn.student_service.modules.common.services;

import com.worksvn.student_service.components.communication.APIs;
import com.worksvn.student_service.constants.CacheValue;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.responses.JobNameDto;
import com.worksvn.common.modules.common.services.BaseJobNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class JobNameService extends BaseJobNameService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    @Override
    public PageDto<JobNameDto> queryJobNameDtos(String name, Boolean matchingName,
                                                List<String> sortBy, List<String> sortType,
                                                Integer pageIndex, Integer pageSize,
                                                Set<Integer> ids) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs
                .PUBLIC_getJobNames(name, matchingName, sortBy, sortType, pageIndex, pageSize, ids));
    }

    @Cacheable(cacheNames = CacheValue.JOB_NAMES_BY_FTS, key = "#name")
    public JobNameDto findJobNameByName(String name) throws Exception {
        PageDto<JobNameDto> pageJobNames = queryJobNameDtos(name, true,
                null, null, null, 1, null);
        if (pageJobNames.getItems().isEmpty()) {
            return null;
        }
        return pageJobNames.getItems().get(0);
    }
}
