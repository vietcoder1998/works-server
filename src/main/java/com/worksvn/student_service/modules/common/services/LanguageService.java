package com.worksvn.student_service.modules.common.services;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.responses.LanguageDto;
import com.worksvn.common.modules.common.services.BaseLanguageService;
import com.worksvn.student_service.components.communication.APIs;
import com.worksvn.student_service.constants.CacheValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class LanguageService extends BaseLanguageService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    @Cacheable(cacheNames = CacheValue.LANGUAGES, key = "#id")
    @Override
    public LanguageDto getLanguageDto(int id) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_getLanguage(id));
    }
}
