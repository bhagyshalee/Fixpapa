package com.fixpapa.ffixpapa.VendorPart.Model.JobsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CancelModel implements Serializable {

    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("cancelledDate")
    @Expose
    private String cancelledDate;
    @SerializedName("cancelledBy")
    @Expose
    private String cancelledBy;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(String cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }
}
