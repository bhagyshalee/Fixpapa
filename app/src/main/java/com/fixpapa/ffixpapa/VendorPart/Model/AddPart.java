package com.fixpapa.ffixpapa.VendorPart.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddPart implements Serializable{
    @SerializedName("partName")
    @Expose
    private String partName;
    @SerializedName("partNumber")
    @Expose
    private String partNumber;
    @SerializedName("partCost")
    @Expose
    private Integer partCost;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Integer getPartCost() {
        return partCost;
    }

    public void setPartCost(Integer partCost) {
        this.partCost = partCost;
    }

}
