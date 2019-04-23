package com.fixpapa.ffixpapa.UserPart.Model.UserLoginData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAddress {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("location")
    @Expose
    private LocationLatLong location;
    @SerializedName("street")
    @Expose
    private String street;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocationLatLong getLocation() {
        return location;
    }

    public void setLocation(LocationLatLong location) {
        this.location = location;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}
