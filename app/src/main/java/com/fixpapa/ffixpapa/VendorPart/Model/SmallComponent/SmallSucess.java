package com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmallSucess {

    @SerializedName("success")
    @Expose
    private SmallDataMsg success;

    public SmallDataMsg getSuccess() {
        return success;
    }

    public void setSuccess(SmallDataMsg success) {
        this.success = success;
    }
}
