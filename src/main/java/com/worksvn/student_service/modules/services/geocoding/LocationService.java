package com.worksvn.student_service.modules.services.geocoding;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.responses.RegionAddress;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public RegionAddress getRegionAddress(double lat, double lon) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_getRegionAddress(lat, lon));
    }

    public RegionAddress findRegionAddress(String address) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_findRegionAddress(address));
    }
}
