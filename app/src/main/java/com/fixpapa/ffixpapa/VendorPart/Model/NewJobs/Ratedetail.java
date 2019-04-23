package com.fixpapa.ffixpapa.VendorPart.Model.NewJobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ratedetail implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("providerId")
    @Expose
    private String providerId;
    @SerializedName("userRating")
    @Expose
    private Float userRating;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("forUser")
    @Expose
    private String forUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Float getUserRating() {
        return userRating;
    }

    public void setUserRating(Float userRating) {
        this.userRating = userRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getForUser() {
        return forUser;
    }

    public void setForUser(String forUser) {
        this.forUser = forUser;
    }
}
