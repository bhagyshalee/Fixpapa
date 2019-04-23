package com.fixpapa.ffixpapa.EngineerPart.EngineerInfoModule;

import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Msg;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessEnDM {
    @SerializedName("data")
    @Expose
    private DataDetailInfo data;
    @SerializedName("msg")
    @Expose
    private Msg msg;

    public DataDetailInfo getData() {
        return data;
    }

    public void setData(DataDetailInfo data) {
        this.data = data;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
