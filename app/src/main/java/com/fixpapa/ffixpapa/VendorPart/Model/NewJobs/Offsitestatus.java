package com.fixpapa.ffixpapa.VendorPart.Model.NewJobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Offsitestatus implements Serializable{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("workDate")
    @Expose
    private String workDate;
    @SerializedName("offsiteStatus")
    @Expose
    private OffsiteStatusModel offsiteStatus;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public OffsiteStatusModel getOffsiteStatus() {
        return offsiteStatus;
    }

    public void setOffsiteStatus(OffsiteStatusModel offsiteStatus) {
        this.offsiteStatus = offsiteStatus;
    }
}
