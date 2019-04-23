package com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Msg implements Serializable{


    @SerializedName("replyCode")
    @Expose
    private String replyCode;
    @SerializedName("replyMessage")
    @Expose
    private String replyMessage;

    public String getReplyCode() {
        return replyCode;
    }

    public void setReplyCode(String replyCode) {
        this.replyCode = replyCode;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }
}
