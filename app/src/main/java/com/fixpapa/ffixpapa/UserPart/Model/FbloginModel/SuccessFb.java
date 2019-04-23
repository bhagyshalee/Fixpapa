package com.fixpapa.ffixpapa.UserPart.Model.FbloginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessFb {

    @SerializedName("success")
    @Expose
    private DataMsgFb success;

    public DataMsgFb getSuccess() {
        return success;
    }

    public void setSuccess(DataMsgFb success) {
        this.success = success;
    }
}
