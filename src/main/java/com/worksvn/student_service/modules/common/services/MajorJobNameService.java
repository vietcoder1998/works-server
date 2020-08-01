package com.worksvn.student_service.modules.common.services;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.student_service.components.communication.APIs;
import com.worksvn.student_service.constants.CacheValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MajorJobNameService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    @Cacheable(cacheNames = CacheValue.MAJORS_JOB_NAMES, key = "#majorIDs.toString()", unless = "#result == null")
    public Set<Integer> getJobNameIDsByMajorIDs(Set<Integer> majorIDs) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_getJobNameIDsByMajorIDs(majorIDs));
    }
}
