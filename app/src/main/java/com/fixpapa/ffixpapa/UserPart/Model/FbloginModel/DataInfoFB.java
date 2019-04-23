package com.fixpapa.ffixpapa.UserPart.Model.FbloginModel;

import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.EndTimeModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.StartTimeModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.RatingModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataInfoFB {

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

    @SerializedName("mobile")
    @Expose
    private String mobile;
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
    @SerializedName("rating")
    @Expose
    private RatingModel rating;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("realm")
    @Expose
    private String realm;
    @SerializedName("username")
    @Expose
    private String username;
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
    @SerializedName("provider")
    @Expose
    private String provider;
    @SerializedName("mobileVerified")
    @Expose
    private Boolean mobileVerified;
    @SerializedName("adminVerifiedStatus")
    @Expose
    private String adminVerifiedStatus;

   /* @SerializedName("customerRating")
    @Expose
    private CustomerRating customerRating;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("mobOtp")
    @Expose
    private MobOtp mobOtp;
    @SerializedName("customerType")
    @Expose
    private String customerType;*/

   /* @SerializedName("customerRating")
    @Expose
    private CustomerRating customerRating;*/

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

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public List<AddressesModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressesModel> addresses) {
        this.addresses = addresses;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getProfileComplete() {
        return isProfileComplete;
    }

    public void setProfileComplete(Boolean profileComplete) {
        isProfileComplete = profileComplete;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Boolean getMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(Boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public String getAdminVerifiedStatus() {
        return adminVerifiedStatus;
    }

    public void setAdminVerifiedStatus(String adminVerifiedStatus) {
        this.adminVerifiedStatus = adminVerifiedStatus;
    }

  /*  public CustomerRating getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(CustomerRating customerRating) {
        this.customerRating = customerRating;
    }*/

}