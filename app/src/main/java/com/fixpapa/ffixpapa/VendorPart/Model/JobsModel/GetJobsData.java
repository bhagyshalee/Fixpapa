package com.fixpapa.ffixpapa.VendorPart.Model.JobsModel;

import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Brand;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CategoryModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CompleteJobModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CustomerModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.VendorModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.VendorProduct;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetJobsData {


    @SerializedName("image")
    @Expose
    private List<String> image = null;
    @SerializedName("address")
    @Expose
    private List<AddressesModel> address = null;
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
    private List<Object> ratedetail = null;
    @SerializedName("addPart")
    @Expose
    private List<Object> addPart = null;
    @SerializedName("otp")
    @Expose
    private Integer otp;
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
    @SerializedName("cancelJob")
    @Expose
    private CancelModel cancelJob;
    @SerializedName("cancelledDate")
    @Expose
    private String cancelledDate;
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
    @SerializedName("vendorAssignedDate")
    @Expose
    private String vendorAssignedDate;
    @SerializedName("custSign")
    @Expose
    private String custSign;
    @SerializedName("completeJob")
    @Expose
    private CompleteJobModel completeJob;
    @SerializedName("duration")
    @Expose
    private String duration;

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<AddressesModel> getAddress() {
        return address;
    }

    public void setAddress(List<AddressesModel> address) {
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

    public List<Object> getRatedetail() {
        return ratedetail;
    }

    public void setRatedetail(List<Object> ratedetail) {
        this.ratedetail = ratedetail;
    }

    public List<Object> getAddPart() {
        return addPart;
    }

    public void setAddPart(List<Object> addPart) {
        this.addPart = addPart;
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

    public CancelModel getCancelJob() {
        return cancelJob;
    }

    public void setCancelJob(CancelModel cancelJob) {
        this.cancelJob = cancelJob;
    }

    public String getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(String cancelledDate) {
        this.cancelledDate = cancelledDate;
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

    public String getVendorAssignedDate() {
        return vendorAssignedDate;
    }

    public void setVendorAssignedDate(String vendorAssignedDate) {
        this.vendorAssignedDate = vendorAssignedDate;
    }

    public String getCustSign() {
        return custSign;
    }

    public void setCustSign(String custSign) {
        this.custSign = custSign;
    }

    public CompleteJobModel getCompleteJob() {
        return completeJob;
    }

    public void setCompleteJob(CompleteJobModel completeJob) {
        this.completeJob = completeJob;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
