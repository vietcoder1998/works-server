package com.worksvn.student_service.modules.services.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LocationResponse {
    @JsonProperty("results")
    private List<Result> results;
    @JsonProperty("status")
    private String status;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RegionNameFullAddress getRegionNameAndFullAddress() {
        RegionNameFullAddress regionNameFullAddress = null;
        String cityType = "administrative_area_level_1";
        for (Result result : results) {
            List<AddressComponent> addressComponents = result.getAddressComponents();
            for (int i = addressComponents.size() - 1; i >= 0; i--) {
                AddressComponent addressComponent = addressComponents.get(i);
                if (addressComponent.getTypes().contains(cityType)) {
                    regionNameFullAddress = new RegionNameFullAddress(addressComponent.getLongName(), result.getFormattedAddress());
                    return regionNameFullAddress;
                }
            }
        }
        return regionNameFullAddress;
    }
}
