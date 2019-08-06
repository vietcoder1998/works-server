package com.worksvn.student_service.modules.services.geocoding;

public class RegionNameFullAddress {
    private String regionName;
    private String fullAddress;

    public RegionNameFullAddress(String regionName, String fullAddress) {
        this.regionName = regionName;
        this.fullAddress = fullAddress;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}
