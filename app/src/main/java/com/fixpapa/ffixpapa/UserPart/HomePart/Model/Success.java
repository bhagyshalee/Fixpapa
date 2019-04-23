package com.fixpapa.ffixpapa.UserPart.HomePart.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Success {


    @SerializedName("success")
    @Expose
    private MsgData success;

    public MsgData getSuccess() {
        return success;
    }

    public void setSuccess(MsgData success) {
        this.success = success;
    }
}
