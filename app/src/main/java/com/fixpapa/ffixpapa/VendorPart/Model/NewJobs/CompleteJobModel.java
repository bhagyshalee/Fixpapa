package com.fixpapa.ffixpapa.VendorPart.Model.NewJobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CompleteJobModel implements Serializable {


    @SerializedName("isPaymentDone")
    @Expose
    private Boolean isPaymentDone;
    @SerializedName("modeOfPayment")
    @Expose
    private String modeOfPayment;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("completedAt")
    @Expose
    private String completedAt;

    public Boolean getIsPaymentDone() {
        return isPaymentDone;
    }

    public void setIsPaymentDone(Boolean isPaymentDone) {
        this.isPaymentDone = isPaymentDone;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

}
