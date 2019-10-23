package com.worksvn.student_service.components.communication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.worksvn.common.components.communication.ISApi;
import com.worksvn.common.components.communication.ISHost;
import com.worksvn.common.modules.common.responses.RegionAddress;
import com.worksvn.common.modules.school.responses.SchoolShortNameDto;
import org.springframework.http.HttpMethod;

public class APIs {
    // AUTH SERVICE ====================================================================================================

    // PUBLIC SERVICE ==================================================================================================

    public static ISApi<Object, RegionAddress> PUBLIC_getRegionAddress(double lat, double lon) {
        ISApi<Object, RegionAddress> api = new ISApi<>(ISHost.PUBLIC_SERVICE,
                HttpMethod.GET, "api/location/regionAddress?lat={lat}&lon={lon}",
                null,
                new TypeReference<RegionAddress>() {
                },
                false);
        api.addParam("lat", lat + "");
        api.addParam("lon", lon + "");
        return api;
    }

    // SCHOOL SERVICE ==================================================================================================

    public static ISApi<Object, SchoolShortNameDto> SCHOOL_getSchoolShortName(String schoolID) {
        ISApi<Object, SchoolShortNameDto> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/{id}/shortName",
                null,
                new TypeReference<SchoolShortNameDto>() {
                },
                true);
        api.addParam("id", schoolID);
        return api;
    }
}
