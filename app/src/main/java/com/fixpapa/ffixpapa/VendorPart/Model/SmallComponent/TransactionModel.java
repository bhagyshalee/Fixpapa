package com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("JobId")
    @Expose
    private String jobId;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("ProviderId")
    @Expose
    private String providerId;
    @SerializedName("modeOfPayment")
    @Expose
    private String modeOfPayment;
    @SerializedName("Amount")
    @Expose
    private Integer amount;
    @SerializedName("VendorId")
    @Expose
    private String vendorId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("paymentDate")
    @Expose
    private String paymentDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

}
