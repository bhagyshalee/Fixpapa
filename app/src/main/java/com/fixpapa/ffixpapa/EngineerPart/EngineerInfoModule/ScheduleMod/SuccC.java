package com.fixpapa.ffixpapa.EngineerPart.EngineerInfoModule.ScheduleMod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuccC implements Serializable {
    @SerializedName("success")
    @Expose
    private SuccDM success;

    public SuccDM getSuccess() {
        return success;
    }

    public void setSuccess(SuccDM success) {
        this.success = success;
    }
}
