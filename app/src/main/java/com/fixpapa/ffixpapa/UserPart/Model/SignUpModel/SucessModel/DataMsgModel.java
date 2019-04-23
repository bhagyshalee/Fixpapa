package com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataMsgModel {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("msg")
    @Expose
    private Msg msg;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
