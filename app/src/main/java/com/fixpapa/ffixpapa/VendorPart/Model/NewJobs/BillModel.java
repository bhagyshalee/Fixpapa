package com.fixpapa.ffixpapa.VendorPart.Model.NewJobs;

import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BillModel implements Serializable {

    @SerializedName("totalAmount")
    @Expose
    private Integer totalAmount;
    @SerializedName("addServiceCost")
    @Expose
    private Integer addServiceCost;

    @SerializedName("addPart")
    @Expose
    private List<AddPart> addPart = null;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("clientResponse")
    @Expose
    private String clientResponse;
    @SerializedName("generatedAt")
    @Expose
    private String generatedAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("discount")
    @Expose
    private Integer discount;

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getAddServiceCost() {
        return addServiceCost;
    }

    public void setAddServiceCost(Integer addServiceCost) {
        this.addServiceCost = addServiceCost;
    }

    public List<AddPart> getAddPart() {
        return addPart;
    }

    public void setAddPart(List<AddPart> addPart) {
        this.addPart = addPart;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getClientResponse() {
        return clientResponse;
    }

    public void setClientResponse(String clientResponse) {
        this.clientResponse = clientResponse;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}