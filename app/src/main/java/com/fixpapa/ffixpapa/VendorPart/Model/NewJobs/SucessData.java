package com.fixpapa.ffixpapa.VendorPart.Model.NewJobs;

import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.Msg;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SucessData implements Serializable {
    @SerializedName("data")
    @Expose
    private List<NewJobsData> data = null;
    @SerializedName("msg")
    @Expose
    private Msg msg;

    public List<NewJobsData> getData() {
        return data;
    }

    public void setData(List<NewJobsData> data) {
        this.data = data;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
