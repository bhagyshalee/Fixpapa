package com.fixpapa.ffixpapa.VendorPart.Model.RatingModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessMM {
    @SerializedName("success")
    @Expose
    private SuccessMD success;

    public SuccessMD getSuccess() {
        return success;
    }

    public void setSuccess(SuccessMD success) {
        this.success = success;
    }
}
