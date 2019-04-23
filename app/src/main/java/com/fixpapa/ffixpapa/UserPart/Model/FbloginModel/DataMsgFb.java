package com.fixpapa.ffixpapa.UserPart.Model.FbloginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataMsgFb {
    @SerializedName("data")
    @Expose
    private UserSccessFB data;

    public UserSccessFB getData() {
        return data;
    }

    public void setData(UserSccessFB data) {
        this.data = data;
    }
}
