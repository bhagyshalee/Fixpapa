package com.fixpapa.ffixpapa.UserPart.HomePart.Model.ChangePass;

import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.EndTimeModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.MobileOtpModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.StartTimeModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.RatingModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuccData {


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
    private List<AddressesModel> addresses = null;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("mobile")
    @Expose
    private String mobile;
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
    @SerializedName("rating")
    @Expose
    private RatingModel rating;
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
    private List<Object> expertiseIds = null;
    @SerializedName("newpurchaseIds")
    @Expose
    private List<Object> newpurchaseIds = null;
    @SerializedName("fullName")
    @Expose
    private String fullName;
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
    private MobileOtpModel mobOtp;
    @SerializedName("customerType")
    @Expose
    private String customerType;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("editMobile")
    @Expose
    private Object editMobile;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("rateThisJob")
    @Expose
    private String rateThisJob;
    @SerializedName("mobileNumber")
    @Expose
    private Object mobileNumber;

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

    public List<AddressesModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressesModel> addresses) {
        this.addresses = addresses;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public RatingModel getRating() {
        return rating;
    }

    public void setRating(RatingModel rating) {
        this.rating = rating;
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

    public List<Object> getExpertiseIds() {
        return expertiseIds;
    }

    public void setExpertiseIds(List<Object> expertiseIds) {
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

    public MobileOtpModel getMobOtp() {
        return mobOtp;
    }

    public void setMobOtp(MobileOtpModel mobOtp) {
        this.mobOtp = mobOtp;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Object getEditMobile() {
        return editMobile;
    }

    public void setEditMobile(Object editMobile) {
        this.editMobile = editMobile;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getRateThisJob() {
        return rateThisJob;
    }

    public void setRateThisJob(String rateThisJob) {
        this.rateThisJob = rateThisJob;
    }

    public Object getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Object mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
