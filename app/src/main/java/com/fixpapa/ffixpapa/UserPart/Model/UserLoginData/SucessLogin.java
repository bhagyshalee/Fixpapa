package com.fixpapa.ffixpapa.UserPart.Model.UserLoginData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SucessLogin {
    @SerializedName("success")
    @Expose
    private UserDataTitle success;
    @SerializedName("msg")
    @Expose
    private Msg msg;

    public UserDataTitle getSuccess() {
        return success;
    }

    public void setSuccess(UserDataTitle success) {
        this.success = success;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
