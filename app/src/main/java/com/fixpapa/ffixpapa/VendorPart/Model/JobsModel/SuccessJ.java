package com.fixpapa.ffixpapa.VendorPart.Model.JobsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessJ {


    @SerializedName("success")
    @Expose
    private SuccessDM success;

    public SuccessDM getSuccess() {
        return success;
    }

    public void setSuccess(SuccessDM success) {
        this.success = success;
    }
}
