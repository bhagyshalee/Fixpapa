package com.fixpapa.ffixpapa.UserPart.Model.NotificationModel;

import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Msg;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuccessDataMsg {
    @SerializedName("data")
    @Expose
    private List<NotiData> data = null;
    @SerializedName("msg")
    @Expose
    private Msg msg;

    public List<NotiData> getData() {
        return data;
    }

    public void setData(List<NotiData> data) {
        this.data = data;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
