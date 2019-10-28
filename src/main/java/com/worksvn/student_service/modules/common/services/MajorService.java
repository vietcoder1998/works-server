package com.worksvn.student_service.modules.common.services;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.responses.MajorDto;
import com.worksvn.common.modules.common.services.BaseMajorService;
import com.worksvn.student_service.components.communication.APIs;
import com.worksvn.student_service.constants.CacheValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MajorService extends BaseMajorService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    @Cacheable(cacheNames = CacheValue.MAJORS, key = "#id")
    @Override
    public MajorDto getMajorDto(int id) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_getMajor(id));
    }
}
