package com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessModel {


    @SerializedName("success")
    @Expose
    private DataMsgModel success;

    public DataMsgModel getSuccess() {
        return success;
    }

    public void setSuccess(DataMsgModel success) {
        this.success = success;
    }

}
