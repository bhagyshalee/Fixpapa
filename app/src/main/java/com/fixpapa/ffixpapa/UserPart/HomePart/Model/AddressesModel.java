package com.fixpapa.ffixpapa.UserPart.HomePart.Model;

import com.fixpapa.ffixpapa.UserPart.Model.UserLoginData.LocationLatLong;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressesModel implements Serializable {


    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("location")
    @Expose
    private LocationLatLong location;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

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
}
