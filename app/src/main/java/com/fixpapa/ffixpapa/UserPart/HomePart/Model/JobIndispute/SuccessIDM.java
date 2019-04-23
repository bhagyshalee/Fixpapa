package com.fixpapa.ffixpapa.UserPart.HomePart.Model.JobIndispute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessIDM {
    @SerializedName("data")
    @Expose
    private DataIM data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public DataIM getData() {
        return data;
    }

    public void setData(DataIM data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
