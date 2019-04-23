package com.fixpapa.ffixpapa.VendorPart.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverViewModule {

    @SerializedName("success")
    @Expose
    private SucessExpModule success;

    public SucessExpModule getSuccess() {
        return success;
    }

    public void setSuccess(SucessExpModule success) {
        this.success = success;
    }
}