package com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel;

import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("image")
    @Expose
    private String image;
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
    @SerializedName("customerType")
    @Expose
    private String customerType;

    @SerializedName("gstNumber")
    @Expose
    private String gstNumber;
    @SerializedName("editMobile")
    @Expose
    private String editMobile;
    @SerializedName("emailId")
    @Expose
    private String emailId;


    /*getAddress*/
    @SerializedName("addresses")
    @Expose
    private List<AddressesModel> addresses = null;

    public List<AddressesModel> getAddresses() {
        return addresses;
    }
    public void setAddresses(List<AddressesModel> addresses) {
        this.addresses = addresses;
    }



    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    public String getEditMobile() {
        return editMobile;
    }

    public void setEditMobile(String editMobile) {
        this.editMobile = editMobile;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

}
