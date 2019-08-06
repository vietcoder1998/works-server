package com.worksvn.student_service.modules.services.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class AddressComponent {
    @JsonProperty("long_name")
    private String longName;
    @JsonProperty("types")
    private Set<String> types;

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }
}
