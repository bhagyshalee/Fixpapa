package com.fixpapa.ffixpapa.EngineerPart.EngineerInfoModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessEn {
    @SerializedName("success")
    @Expose
    private SuccessEnDM success;

    public SuccessEnDM getSuccess() {
        return success;
    }

    public void setSuccess(SuccessEnDM success) {
        this.success = success;
    }
}
