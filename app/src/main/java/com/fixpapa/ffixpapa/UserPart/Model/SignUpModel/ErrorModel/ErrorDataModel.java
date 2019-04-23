package com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.ErrorModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorDataModel {


    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("stack")
    @Expose
    private String stack;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

}
