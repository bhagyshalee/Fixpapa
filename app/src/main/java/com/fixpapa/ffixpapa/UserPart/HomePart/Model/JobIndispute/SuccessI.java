package com.fixpapa.ffixpapa.UserPart.HomePart.Model.JobIndispute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessI {
    @SerializedName("success")
    @Expose
    private SuccessIDM success;

    public SuccessIDM getSuccess() {
        return success;
    }

    public void setSuccess(SuccessIDM success) {
        this.success = success;
    }
}
