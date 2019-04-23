package com.fixpapa.ffixpapa.VendorPart.Model.RatingModel;

import com.fixpapa.ffixpapa.UserPart.HomePart.Model.EndTimeModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.StartTimeModel;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.MobOtp;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.RatingModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataInfo {

    @SerializedName("startTime")
    @Expose
    private StartTimeModel startTime;
    @SerializedName("endTime")
    @Expose
    private EndTimeModel endTime;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("isAvailable")
    @Expose
    private Boolean isAvailable;
    @SerializedName("addresses")
    @Expose
    private List<Object> addresses = null;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("newNotification")
    @Expose
    private Integer newNotification;
    @SerializedName("isProfileComplete")
    @Expose
    private Boolean isProfileComplete;
    @SerializedName("gstNumber")
    @Expose
    private String gstNumber;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("realm")
    @Expose
    private String realm;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("emailVerified")
    @Expose
    private Boolean emailVerified;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("servicesIds")
    @Expose
    private List<Object> servicesIds = null;
    @SerializedName("expertiseIds")
    @Expose
    private List<String> expertiseIds = null;
    @SerializedName("newpurchaseIds")
    @Expose
    private List<Object> newpurchaseIds = null;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("adminVerifiedStatus")
    @Expose
    private String adminVerifiedStatus;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("mobileVerified")
    @Expose
    private Boolean mobileVerified;
    @SerializedName("mobOtp")
    @Expose
    private MobOtp mobOtp;
    @SerializedName("vendorId")
    @Expose
    private String vendorId;
    @SerializedName("exp")
    @Expose
    private Integer exp;
    @SerializedName("isJobAssigned")
    @Expose
    private Boolean isJobAssigned;
    @SerializedName("rating")
    @Expose
    private RatingModel rating;
    @SerializedName("rateThisJob")
    @Expose
    private Object rateThisJob;

    public StartTimeModel getStartTime() {
        return startTime;
    }

    public void setStartTime(StartTimeModel startTime) {
        this.startTime = startTime;
    }

    public EndTimeModel getEndTime() {
        return endTime;
    }

    public void setEndTime(EndTimeModel endTime) {
        this.endTime = endTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public List<Object> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Object> addresses) {
        this.addresses = addresses;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getNewNotification() {
        return newNotification;
    }

    public void setNewNotification(Integer newNotification) {
        this.newNotification = newNotification;
    }

    public Boolean getIsProfileComplete() {
        return isProfileComplete;
    }

    public void setIsProfileComplete(Boolean isProfileComplete) {
        this.isProfileComplete = isProfileComplete;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Object> getServicesIds() {
        return servicesIds;
    }

    public void setServicesIds(List<Object> servicesIds) {
        this.servicesIds = servicesIds;
    }

    public List<String> getExpertiseIds() {
        return expertiseIds;
    }

    public void setExpertiseIds(List<String> expertiseIds) {
        this.expertiseIds = expertiseIds;
    }

    public List<Object> getNewpurchaseIds() {
        return newpurchaseIds;
    }

    public void setNewpurchaseIds(List<Object> newpurchaseIds) {
        this.newpurchaseIds = newpurchaseIds;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAdminVerifiedStatus() {
        return adminVerifiedStatus;
    }

    public void setAdminVerifiedStatus(String adminVerifiedStatus) {
        this.adminVerifiedStatus = adminVerifiedStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(Boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public MobOtp getMobOtp() {
        return mobOtp;
    }

    public void setMobOtp(MobOtp mobOtp) {
        this.mobOtp = mobOtp;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Boolean getIsJobAssigned() {
        return isJobAssigned;
    }

    public void setIsJobAssigned(Boolean isJobAssigned) {
        this.isJobAssigned = isJobAssigned;
    }

    public RatingModel getRating() {
        return rating;
    }

    public void setRating(RatingModel rating) {
        this.rating = rating;
    }

    public Object getRateThisJob() {
        return rateThisJob;
    }

    public void setRateThisJob(Object rateThisJob) {
        this.rateThisJob = rateThisJob;
    }

}
