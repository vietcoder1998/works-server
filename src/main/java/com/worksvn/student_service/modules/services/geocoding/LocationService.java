package com.worksvn.student_service.modules.services.geocoding;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.exceptions.ISResponseException;
import com.worksvn.common.modules.common.responses.RegionAddress;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public RegionAddress getRegionAddress(double lat, double lon) throws Exception {
        RegionAddress regionAddress = null;
        ISRestCommunicator.ExchangeResult<RegionAddress> result = restCommunicator
                .exchange(APIs.PUBLIC_getRegionAddress(lat, lon));
        if (result.isSuccess()) {
            regionAddress = result.getConvertedBody().getData();
        } else {
            throw new ISResponseException(result);
        }
        return regionAddress;
    }
}
