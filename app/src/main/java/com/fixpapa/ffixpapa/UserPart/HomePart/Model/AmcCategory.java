package com.fixpapa.ffixpapa.UserPart.HomePart.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AmcCategory implements Serializable,Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    protected AmcCategory(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<AmcCategory> CREATOR = new Creator<AmcCategory>() {
        @Override
        public AmcCategory createFromParcel(Parcel in) {
            return new AmcCategory(in);
        }

        @Override
        public AmcCategory[] newArray(int size) {
            return new AmcCategory[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void readFromParcel(Parcel in) {
        name=in.readString();
        id=in.readString();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
    }
}
