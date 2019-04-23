package com.fixpapa.ffixpapa.VendorPart.Model.NewJobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Problems implements Serializable {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("categoryId")
        @Expose
        private String categoryId;
        @SerializedName("probContent")
        @Expose
        private String probContent;
        @SerializedName("price")
        @Expose
        private Integer price;

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

        public String getProbContent() {
            return probContent;
        }

        public void setProbContent(String probContent) {
            this.probContent = probContent;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

}
