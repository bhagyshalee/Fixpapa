package com.fixpapa.ffixpapa.EngineerPart.EngineerInfoModule.ScheduleMod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuccDM implements Serializable {

    @SerializedName("data")
    @Expose
    private SucceDA data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public SucceDA getData() {
        return data;
    }

    public void setData(SucceDA data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
