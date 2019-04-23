package com.fixpapa.ffixpapa.UserPart.HomePart.Model.ChangePass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccDM {
    @SerializedName("data")
    @Expose
    private SuccData data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public SuccData getData() {
        return data;
    }

    public void setData(SuccData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
