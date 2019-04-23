package com.fixpapa.ffixpapa.VendorPart.Model;

import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Brand;
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
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.VendorModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.VendorProduct;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.TransactionModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SampleData implements Serializable {

    private List<String> image = null;

    private AddressesModel address;

    private Integer totalPrice;

    private String duration;

    private String problemDes;

    private String startDate;

    private String endDate;

    private List<Problems> problems = null;

    private String orderId;

    private String id;

    private String categoryId;

    private String productId;

    private String brandId;

    private String customerId;

    private String vendorId;

    private String engineerId;

    private Integer otp;

    private String status;

    private Boolean isVendorAssigned;

    private Boolean isEngineerAssigned;

    private String siteType;

    private BillModel bill;

    private String createdAt;

    private String updatedAt;

    private CategoryModel category;

    private VendorProduct product;

    private Brand brand;

    private EngineerModel engineer;
    private List<Ratedetail> ratedetail;

    public List<Ratedetail> getRatedetail() {
        return ratedetail;
    }

    public void setRatedetail(List<Ratedetail> ratedetail) {
        this.ratedetail = ratedetail;
    }

    private VendorModel vendor;
    private CustomerModel customer;

    private String vendorAssignedDate;

    private CancelModel cancelJob;

    private ScheduleModel schedule;
    private String customerName;
    private PickModel pick;
    private String CustomerMobile;
    private String modeOfPayment;
    private CompleteJobModel completeJob;
    private TransactionModel transactionModel;

    public TransactionModel getTransactionModel() {
        return transactionModel;
    }

    public void setTransactionModel(TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
    }

    public CompleteJobModel getCompleteJob() {
        return completeJob;
    }

    public void setCompleteJob(CompleteJobModel completeJob) {
        this.completeJob = completeJob;
    }

    public String getCustomerMobile() {
        return CustomerMobile;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public void setCustomerMobile(String customerMobile) {
        CustomerMobile = customerMobile;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public CancelModel getCancelJob() {
        return cancelJob;
    }

    public void setCancelJob(CancelModel cancelJob) {
        this.cancelJob = cancelJob;
    }

    public ScheduleModel getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleModel schedule) {
        this.schedule = schedule;
    }
}
