package com.fixpapa.ffixpapa.UserPart.HomePart.Model.ChangePass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Succ {
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
