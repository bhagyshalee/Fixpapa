package com.fixpapa.ffixpapa.UserPart.Model.NotificationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessNoti {
    @SerializedName("success")
    @Expose
    private SuccessDataMsg success;

    public SuccessDataMsg getSuccess() {
        return success;
    }

    public void setSuccess(SuccessDataMsg success) {
        this.success = success;
    }

}
