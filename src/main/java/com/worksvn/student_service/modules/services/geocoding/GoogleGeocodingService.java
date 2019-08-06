package com.worksvn.student_service.modules.services.geocoding;

import com.worksvn.student_service.modules.common.models.dtos.RegionAddress;
import com.worksvn.student_service.modules.common.models.entities.Region;
import com.worksvn.student_service.modules.common.repositories.RegionRepository;
import com.worksvn.student_service.utils.base.HttpGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleGeocodingService {
    private static Logger logger = LoggerFactory.getLogger(GoogleGeocodingService.class);

    @Value("${application.google.geocoding.api}")
    public String api;
    @Value("${application.google.geocoding.api-key}")
    public String apiKey;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RegionRepository regionRepository;

    public RegionAddress fetchRegionAndAddress(double lat, double lon) {
        LocationResponse locationResponse = fetchAddress(lat, lon);
        if (locationResponse == null) {
            return null;
        }
        RegionNameFullAddress regionNameFullAddress = locationResponse.getRegionNameAndFullAddress();
        Region region = regionRepository.findFirstByName(regionNameFullAddress.getRegionName());
        return new RegionAddress(lat, lon, region, regionNameFullAddress.getFullAddress());
    }

    private LocationResponse fetchAddress(double lat, double lon) {
        try {
            return new HttpGetRequest(restTemplate)
                    .withParam("key", apiKey)
                    .withParam("latlng", lat + "," + lon)
                    .withParam("sensor", true)
                    .withUrl(api)
                    .execute(LocationResponse.class);
        } catch (Exception e) {
            if (e instanceof HttpStatusCodeException) {
                logger.error("fetchAddress", ((HttpStatusCodeException) e).getResponseBodyAsString());
            } else {
                logger.error("fetchAddress", e);
            }
            return null;
        }
    }
}
