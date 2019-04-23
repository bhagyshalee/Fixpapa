package com.fixpapa.ffixpapa.VendorPart.Model.JobsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ScheduleModel implements Serializable{

    @SerializedName("eStartDate")
    @Expose
    private String eStartDate;
    @SerializedName("eEndDate")
    @Expose
    private String eEndDate;

    public String getEStartDate() {
        return eStartDate;
    }

    public void setEStartDate(String eStartDate) {
        this.eStartDate = eStartDate;
    }

    public String getEEndDate() {
        return eEndDate;
    }

    public void setEEndDate(String eEndDate) {
        this.eEndDate = eEndDate;
    }
}
