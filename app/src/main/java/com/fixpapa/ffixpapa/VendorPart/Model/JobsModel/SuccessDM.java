package com.fixpapa.ffixpapa.VendorPart.Model.JobsModel;

import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.Msg;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuccessDM {


    @SerializedName("data")
    @Expose
    private List<GetJobsData> data = null;
    @SerializedName("msg")
    @Expose
    private Msg msg;

    public List<GetJobsData> getData() {
        return data;
    }

    public void setData(List<GetJobsData> data) {
        this.data = data;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
