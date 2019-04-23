package com.fixpapa.ffixpapa.VendorPart.Model.EngineerModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SucessEngineerModel implements Serializable{


    @SerializedName("success")
    @Expose
    private SuccessGetData success;

    public SuccessGetData getSuccess() {
        return success;
    }

    public void setSuccess(SuccessGetData success) {
        this.success = success;
    }
}
