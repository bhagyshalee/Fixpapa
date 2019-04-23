package com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent;

import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.Msg;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmallDataMsg {

    @SerializedName("data")
    @Expose
    private SmallDataInfo data;
    @SerializedName("msg")
    @Expose
    private Msg msg;

    public SmallDataInfo getData() {
        return data;
    }

    public void setData(SmallDataInfo data) {
        this.data = data;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
