package com.worksvn.student_service.modules.school.services;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.exceptions.ISResponseException;
import com.worksvn.common.modules.school.responses.SchoolShortNameDto;
import com.worksvn.student_service.components.communication.APIs;
import com.worksvn.student_service.constants.CacheValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SchoolService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    @Cacheable(cacheNames = CacheValue.SCHOOL_SHORT_NAMES, key = "#schoolID")
    public String getSchoolShortName(String schoolID) throws Exception {
        ISRestCommunicator.ExchangeResult<SchoolShortNameDto> result = restCommunicator
                .exchange(APIs.SCHOOL_getSchoolShortName(schoolID));
        if (result.isSuccess()) {
            return result.getConvertedBody().getData().getShortName();
        } else {
            throw new ISResponseException(result);
        }
    }
}
