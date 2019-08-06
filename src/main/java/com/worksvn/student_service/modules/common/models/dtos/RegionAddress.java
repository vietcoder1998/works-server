package com.worksvn.student_service.modules.common.models.dtos;

import com.worksvn.student_service.modules.common.models.entities.Region;

public class RegionAddress {
    private double lat;
    private double lon;
    private Region region;
    private String address;

    public RegionAddress(double lat, double lon,
                         Region region, String address) {
        this.lat = lat;
        this.lon = lon;
        this.region = region;
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
