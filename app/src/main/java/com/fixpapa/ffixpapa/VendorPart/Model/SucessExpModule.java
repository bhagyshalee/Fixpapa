package com.fixpapa.ffixpapa.VendorPart.Model;

import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Msg;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SucessExpModule {
    @SerializedName("data")
    @Expose
    private List<GetDataModle> engineerRegi = null;

    @SerializedName("msg")
    @Expose
    private Msg msg;

    public List<GetDataModle> getEngineerRegi() {
        return engineerRegi;
    }

    public void setEngineerRegi(List<GetDataModle> engineerRegi) {
        this.engineerRegi = engineerRegi;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}