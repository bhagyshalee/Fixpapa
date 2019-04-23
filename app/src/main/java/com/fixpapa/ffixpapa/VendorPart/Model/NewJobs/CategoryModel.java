package com.fixpapa.ffixpapa.VendorPart.Model.NewJobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CategoryModel implements Serializable {


    @SerializedName("isAvailableForRent")
    @Expose
    private Boolean isAvailableForRent;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("deleted")
    @Expose
    private Boolean deleted;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("productsIds")
    @Expose
    private List<String> productsIds = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public Boolean getIsAvailableForRent() {
        return isAvailableForRent;
    }

    public void setIsAvailableForRent(Boolean isAvailableForRent) {
        this.isAvailableForRent = isAvailableForRent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getProductsIds() {
        return productsIds;
    }

    public void setProductsIds(List<String> productsIds) {
        this.productsIds = productsIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}