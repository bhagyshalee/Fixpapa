package com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent;

import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Brand;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.EndTimeModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.MobileOtpModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.StartTimeModel;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.CancelModel;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.EngineerModel;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.ScheduleModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CategoryModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CompleteJobModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CustomerModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.PickModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Ratedetail;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.RatingModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.VendorModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.VendorProduct;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SmallDataInfo {

    @SerializedName("image")
    @Expose
    private List<String> image = null;
    @SerializedName("address")
    @Expose
    private AddressesModel address;
    @SerializedName("totalPrice")
    @Expose
    private Integer totalPrice;
    @SerializedName("problemDes")
    @Expose
    private String problemDes;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("problems")
    @Expose
    private List<Problems> problems = null;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("brandId")
    @Expose
    private String brandId;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("vendorId")
    @Expose
    private String vendorId;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("ratedetail")
    @Expose
    private List<Ratedetail> ratedetail = null;
    @SerializedName("isVendorAssigned")
    @Expose
    private Boolean isVendorAssigned;
    @SerializedName("isEngineerAssigned")
    @Expose
    private Boolean isEngineerAssigned;
    @SerializedName("siteType")
    @Expose
    private String siteType;
    @SerializedName("bill")
    @Expose
    private BillModel bill;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("vendorAssignedDate")
    @Expose
    private String vendorAssignedDate;

    @SerializedName("custSign")
    @Expose
    private String custSign;
    @SerializedName("pick")
    @Expose
    private PickModel pick;
    @SerializedName("engineerId")
    @Expose
    private String engineerId;

    @SerializedName("completeJob")
    @Expose
    private CompleteJobModel completeJob;


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
    @SerializedName("realm")
    @Expose
    private String realm;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("emailVerified")
    @Expose
    private Boolean emailVerified;
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
    @SerializedName("mobileVerified")
    @Expose
    private Boolean mobileVerified;
    @SerializedName("mobOtp")
    @Expose
    private MobileOtpModel mobOtp;
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
    @SerializedName("category")
    @Expose
    private CategoryModel category;
    @SerializedName("product")
    @Expose
    private VendorProduct product;
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("engineer")
    @Expose
    private EngineerModel engineer;
    @SerializedName("transaction")
    @Expose
    private TransactionModel transaction;
    @SerializedName("schedule")
    @Expose
    private ScheduleModel schedule;

    @SerializedName("vendor")
    @Expose
    private VendorModel vendor;
    @SerializedName("customer")
    @Expose
    private CustomerModel customer;


    @SerializedName("cancelJob")
    @Expose
    private CancelModel cancelJob;

    public CancelModel getCancelJob() {
        return cancelJob;
    }

    public void setCancelJob(CancelModel cancelJob) {
        this.cancelJob = cancelJob;
    }

    public VendorModel getVendor() {
        return vendor;
    }

    public void setVendor(VendorModel vendor) {
        this.vendor = vendor;
    }

    public CustomerModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
    }

    public ScheduleModel getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleModel schedule) {
        this.schedule = schedule;
    }

    public TransactionModel getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionModel transaction) {
        this.transaction = transaction;
    }

    public EngineerModel getEngineer() {
        return engineer;
    }

    public void setEngineer(EngineerModel engineer) {
        this.engineer = engineer;
    }

    public Boolean getVendorAssigned() {
        return isVendorAssigned;
    }

    public void setVendorAssigned(Boolean vendorAssigned) {
        isVendorAssigned = vendorAssigned;
    }

    public Boolean getEngineerAssigned() {
        return isEngineerAssigned;
    }

    public void setEngineerAssigned(Boolean engineerAssigned) {
        isEngineerAssigned = engineerAssigned;
    }

    public void setStartTime(StartTimeModel startTime) {
        this.startTime = startTime;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Boolean getProfileComplete() {
        return isProfileComplete;
    }

    public void setProfileComplete(Boolean profileComplete) {
        isProfileComplete = profileComplete;
    }

    public Boolean getJobAssigned() {
        return isJobAssigned;
    }

    public void setJobAssigned(Boolean jobAssigned) {
        isJobAssigned = jobAssigned;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public VendorProduct getProduct() {
        return product;
    }

    public void setProduct(VendorProduct product) {
        this.product = product;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public StartTimeModel getStartTime() {
        return startTime;
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

    public CompleteJobModel getCompleteJob() {
        return completeJob;
    }

    public void setCompleteJob(CompleteJobModel completeJob) {
        this.completeJob = completeJob;
    }

    public String getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(String engineerId) {
        this.engineerId = engineerId;
    }

    public String getCustSign() {
        return custSign;
    }

    public void setCustSign(String custSign) {
        this.custSign = custSign;
    }

    public PickModel getPick() {
        return pick;
    }

    public void setPick(PickModel pick) {
        this.pick = pick;
    }
    public List<Ratedetail> getRatedetail() {
        return ratedetail;
    }

    public void setRatedetail(List<Ratedetail> ratedetail) {
        this.ratedetail = ratedetail;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public AddressesModel getAddress() {
        return address;
    }

    public void setAddress(AddressesModel address) {
        this.address = address;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProblemDes() {
        return problemDes;
    }

    public void setProblemDes(String problemDes) {
        this.problemDes = problemDes;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Problems> getProblems() {
        return problems;
    }

    public void setProblems(List<Problems> problems) {
        this.problems = problems;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsVendorAssigned() {
        return isVendorAssigned;
    }

    public void setIsVendorAssigned(Boolean isVendorAssigned) {
        this.isVendorAssigned = isVendorAssigned;
    }

    public Boolean getIsEngineerAssigned() {
        return isEngineerAssigned;
    }

    public void setIsEngineerAssigned(Boolean isEngineerAssigned) {
        this.isEngineerAssigned = isEngineerAssigned;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public BillModel getBill() {
        return bill;
    }

    public void setBill(BillModel bill) {
        this.bill = bill;
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

    public String getVendorAssignedDate() {
        return vendorAssignedDate;
    }

    public void setVendorAssignedDate(String vendorAssignedDate) {
        this.vendorAssignedDate = vendorAssignedDate;
    }

}
