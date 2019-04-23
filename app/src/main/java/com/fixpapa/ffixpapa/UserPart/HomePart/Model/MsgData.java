package com.fixpapa.ffixpapa.UserPart.HomePart.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MsgData {


    @SerializedName("data")
    @Expose
    private AllHomeArray data;
    @SerializedName("msg")
    @Expose
    private Msg msg;

    public AllHomeArray getData() {
        return data;
    }

    public void setData(AllHomeArray data) {
        this.data = data;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
