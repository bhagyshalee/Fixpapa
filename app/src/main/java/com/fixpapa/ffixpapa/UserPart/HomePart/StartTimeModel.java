package com.fixpapa.ffixpapa.UserPart.HomePart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartTimeModel {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("valueInMinute")
    @Expose
    private Integer valueInMinute;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getValueInMinute() {
        return valueInMinute;
    }

    public void setValueInMinute(Integer valueInMinute) {
        this.valueInMinute = valueInMinute;
    }
}
