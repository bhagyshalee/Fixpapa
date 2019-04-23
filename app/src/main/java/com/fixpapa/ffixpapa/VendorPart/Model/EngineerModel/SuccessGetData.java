package com.fixpapa.ffixpapa.VendorPart.Model.EngineerModel;

import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.Msg;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessGetData implements Serializable {

    @SerializedName("data")
    @Expose
    private List<GetEngineerData> data = null;
    @SerializedName("msg")
    @Expose
    private Msg msg;

    public List<GetEngineerData> getData() {
        return data;
    }

    public void setData(List<GetEngineerData> data) {
        this.data = data;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
