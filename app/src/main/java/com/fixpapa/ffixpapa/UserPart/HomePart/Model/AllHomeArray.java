package com.fixpapa.ffixpapa.UserPart.HomePart.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllHomeArray implements Serializable {

    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("newpurchases")
    @Expose
    private List<Newpurchase> newpurchases = null;
    @SerializedName("amc")
    @Expose
    private List<Amc> amc = null;
    @SerializedName("bid")
    @Expose
    private List<Bid> bid = null;
    @SerializedName("rent")
    @Expose
    private List<Rent> rent = null;


    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Newpurchase> getNewpurchases() {
        return newpurchases;
    }

    public void setNewpurchases(List<Newpurchase> newpurchases) {
        this.newpurchases = newpurchases;
    }

    public List<Amc> getAmc() {
        return amc;
    }

    public void setAmc(List<Amc> amc) {
        this.amc = amc;
    }

    public List<Bid> getBid() {
        return bid;
    }

    public void setBid(List<Bid> bid) {
        this.bid = bid;
    }

    public List<Rent> getRent() {
        return rent;
    }

    public void setRent(List<Rent> rent) {
        this.rent = rent;
    }

}