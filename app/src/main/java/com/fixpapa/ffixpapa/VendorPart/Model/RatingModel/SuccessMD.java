package com.fixpapa.ffixpapa.VendorPart.Model.RatingModel;

import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.Msg;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessMD {
    @SerializedName("data")
    @Expose
    private DataInfo data;
    @SerializedName("msg")
    @Expose
    private Msg msg;

    public DataInfo getData() {
        return data;
    }

    public void setData(DataInfo data) {
        this.data = data;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
