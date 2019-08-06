package com.worksvn.student_service.modules.services.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Result {
    @JsonProperty("address_components")
    private List<AddressComponent> addressComponents;
    @JsonProperty("formatted_address")
    private String formattedAddress;


    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getFormattedAddress() {
        int size = addressComponents.size();
        formattedAddress = formattedAddress.replace(", " + addressComponents.get(size - 1).getLongName(), "")
                .replace( "Unnamed Road, ", "");
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }
}
