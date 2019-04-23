package com.fixpapa.ffixpapa.VendorPart.Model.NewJobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingModel {
    @SerializedName("totalUsers")
    @Expose
    private Integer totalUsers;
    @SerializedName("totalRating")
    @Expose
    private Double totalRating;
    @SerializedName("avgRating")
    @Expose
    private Double avgRating;

    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

}
