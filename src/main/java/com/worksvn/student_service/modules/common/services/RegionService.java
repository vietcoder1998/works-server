package com.worksvn.student_service.modules.common.services;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.responses.RegionDto;
import com.worksvn.common.modules.common.services.BaseRegionService;
import com.worksvn.student_service.components.communication.APIs;
import com.worksvn.student_service.constants.CacheValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RegionService extends BaseRegionService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    @Cacheable(cacheNames = CacheValue.REGIONS, key = "#id")
    @Override
    public RegionDto getRegionDto(int id) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_getRegion(id));
    }
}
