package com.fixpapa.ffixpapa.VendorPart.Model.NewJobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuccessModelVendor implements Serializable{

    @SerializedName("success")
    @Expose
    private SucessData success;

    public SucessData getSuccess() {
        return success;
    }

    public void setSuccess(SucessData success) {
        this.success = success;
    }
}
