package com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobOtp {


    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("expireAt")
    @Expose
    private String expireAt;
    @SerializedName("otp")
    @Expose
    private Integer otp;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(String expireAt) {
        this.expireAt = expireAt;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

}
