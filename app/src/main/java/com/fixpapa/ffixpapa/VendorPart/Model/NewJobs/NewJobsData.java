package com.fixpapa.ffixpapa.VendorPart.Model.NewJobs;

import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Brand;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.CancelModel;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.EngineerModel;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.ScheduleModel;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.TransactionModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NewJobsData implements Serializable{

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
    @SerializedName("engineerId")
    @Expose
    private String engineerId;
    @SerializedName("ratedetail")
    @Expose
    private List<Ratedetail> ratedetail = null;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("otp")
    @Expose
    private Object otp;
    @SerializedName("status")
    @Expose
    private String status;
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
    @SerializedName("schedule")
    @Expose
    private ScheduleModel schedule;
    @SerializedName("cancelJob")
    @Expose
    private CancelModel cancelJob;
    @SerializedName("category")
    @Expose
    private CategoryModel category;
    @SerializedName("product")
    @Expose
    private VendorProduct product;
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("engineer")
    @Expose
    private EngineerModel engineer;
    @SerializedName("vendor")
    @Expose
    private VendorModel vendor;
    @SerializedName("customer")
    @Expose
    private CustomerModel customer;
    @SerializedName("custSign")
    @Expose
    private String custSign;

    @SerializedName("pick")
    @Expose
    private PickModel pick;

    @SerializedName("completeJob")
    @Expose
    private CompleteJobModel completeJob;

    @SerializedName("transaction")
    @Expose
    private TransactionModel transactionModel;

    @SerializedName("modeOfPayment")
    @Expose
    private String modeOfPayment;

    public TransactionModel getTransactionModel() {
        return transactionModel;
    }

    public void setTransactionModel(TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
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

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public CompleteJobModel getCompleteJob() {
        return completeJob;
    }

    public void setCompleteJob(CompleteJobModel completeJob) {
        this.completeJob = completeJob;
    }

    public PickModel getPick() {
        return pick;
    }

    public void setPick(PickModel pick) {
        this.pick = pick;
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

    public String getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(String engineerId) {
        this.engineerId = engineerId;
    }

    public List<Ratedetail> getRatedetail() {
        return ratedetail;
    }

    public void setRatedetail(List<Ratedetail> ratedetail) {
        this.ratedetail = ratedetail;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public Object getOtp() {
        return otp;
    }

    public void setOtp(Object otp) {
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

    public ScheduleModel getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleModel schedule) {
        this.schedule = schedule;
    }

    public CancelModel getCancelJob() {
        return cancelJob;
    }

    public void setCancelJob(CancelModel cancelJob) {
        this.cancelJob = cancelJob;
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

    public EngineerModel getEngineer() {
        return engineer;
    }

    public void setEngineer(EngineerModel engineer) {
        this.engineer = engineer;
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

    public String getCustSign() {
        return custSign;
    }

    public void setCustSign(String custSign) {
        this.custSign = custSign;
    }

}