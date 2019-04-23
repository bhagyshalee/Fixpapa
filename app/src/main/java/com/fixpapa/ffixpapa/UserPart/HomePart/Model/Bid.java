package com.fixpapa.ffixpapa.UserPart.HomePart.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Bid implements Parcelable,Serializable {


    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("servicesIds")
    @Expose
    private List<String> servicesIds = null;
    @SerializedName("noOfSystems")
    @Expose
    private List<String> noOfSystems = null;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("services")
    @Expose
    private List<AmcCategory> services = null;

    protected Bid(Parcel in) {
        name = in.readString();
        servicesIds = in.createStringArrayList();
        noOfSystems = in.createStringArrayList();
        image = in.readString();
        id = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<Bid> CREATOR = new Creator<Bid>() {
        @Override
        public Bid createFromParcel(Parcel in) {
            return new Bid(in);
        }

        @Override
        public Bid[] newArray(int size) {
            return new Bid[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getServicesIds() {
        return servicesIds;
    }

    public void setServicesIds(List<String> servicesIds) {
        this.servicesIds = servicesIds;
    }

    public List<String> getNoOfSystems() {
        return noOfSystems;
    }

    public void setNoOfSystems(List<String> noOfSystems) {
        this.noOfSystems = noOfSystems;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<AmcCategory> getServices() {
        return services;
    }

    public void setServices(List<AmcCategory> services) {
        this.services = services;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel in) {
      name=in.readString();
      image=in.readString();
      id=in.readString();
      in.readStringList(servicesIds);
      in.readStringList(noOfSystems);


    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(name);
       dest.writeString(image);
       dest.writeString(id);
       dest.writeStringList(servicesIds);
       dest.writeStringList(noOfSystems);
    }
}