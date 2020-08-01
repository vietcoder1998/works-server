package com.worksvn.student_service.modules.school.services;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.school.responses.SimpleSchoolPreview;
import com.worksvn.common.modules.school.services.BaseSchoolService;
import com.worksvn.student_service.components.communication.APIs;
import com.worksvn.student_service.constants.CacheValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SchoolService extends BaseSchoolService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    @Cacheable(cacheNames = CacheValue.SCHOOL_SHORT_NAMES, key = "#schoolID", unless = "#result == null")
    public String getSchoolShortName(String schoolID) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.SCHOOL_getSchoolShortName(schoolID)).getShortName();
    }

    @Cacheable(cacheNames = CacheValue.SIMPLE_SCHOOL_PREVIEWS, key = "#schoolID", unless = "#result == null")
    @Override
    public SimpleSchoolPreview getSimpleSchoolPreview(String schoolID) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.SCHOOL_getSchoolSimpleInfo(schoolID));
    }

    public void checkSchoolExist(String schoolID) throws Exception {
        restCommunicator.exchangeForSuccess(APIs.SCHOOL_checkSchoolExists(schoolID));
    }
}
