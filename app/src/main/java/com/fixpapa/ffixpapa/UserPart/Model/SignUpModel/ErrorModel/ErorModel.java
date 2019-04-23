package com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.ErrorModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErorModel {

    @SerializedName("error")
    @Expose
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
