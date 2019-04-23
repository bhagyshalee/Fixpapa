package com.fixpapa.ffixpapa.VendorPart.Model.NewJobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PickModel implements Serializable{

    @SerializedName("proCondition")
    @Expose
    private String proCondition;
    @SerializedName("damage")
    @Expose
    private String damage;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("offsiteStatus")
    @Expose
    private List<Offsitestatus> offsiteStatus = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public String getCustSign() {
        return custSign;
    }

    public void setCustSign(String custSign) {
        this.custSign = custSign;
    }

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @SerializedName("custSign")
    @Expose
    private String custSign;

    public String getProCondition() {
        return proCondition;
    }

    public void setProCondition(String proCondition) {
        this.proCondition = proCondition;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<Offsitestatus> getOffsiteStatus() {
        return offsiteStatus;
    }

    public void setOffsiteStatus(List<Offsitestatus> offsiteStatus) {
        this.offsiteStatus = offsiteStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
