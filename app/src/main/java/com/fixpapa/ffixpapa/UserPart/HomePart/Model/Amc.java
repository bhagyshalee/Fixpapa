package com.fixpapa.ffixpapa.UserPart.HomePart.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Amc implements Serializable,Parcelable{


    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("noOfUnits")
    @Expose
    private List<String> noOfUnits = null;
    @SerializedName("categoryIds")
    @Expose
    private List<String> categoryIds = null;
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
    @SerializedName("category")
    @Expose
    private List<AmcCategory> category = null;

    protected Amc(Parcel in) {
        name = in.readString();
        noOfUnits = in.createStringArrayList();
        categoryIds = in.createStringArrayList();
        image = in.readString();
        id = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        category = in.createTypedArrayList(AmcCategory.CREATOR);
    }
    public static final Creator<Amc> CREATOR = new Creator<Amc>() {
        @Override
        public Amc createFromParcel(Parcel in) {
            return new Amc(in);
        }

        @Override
        public Amc[] newArray(int size) {
            return new Amc[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(List<String> noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
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

    public List<AmcCategory> getCategory() {
        return category;
    }

    public void setCategory(List<AmcCategory> category) {
        this.category = category;

}

    public void readFromParcel(Parcel in) {
       name=in.readString();
       id=in.readString();
       image=in.readString();
       in.readStringList(noOfUnits);
       in.readStringList(categoryIds);
       in.readTypedList(category,AmcCategory.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(image);
        dest.writeTypedList(category);
        dest.writeStringList(noOfUnits);
        dest.writeStringList(categoryIds);

    }
}
