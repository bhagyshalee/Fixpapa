package com.fixpapa.ffixpapa.UserPart.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryNew implements Serializable {
    String name;
    ArrayList<String> products;
    ArrayList<ArrayList<String>> brands;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public ArrayList<ArrayList<String>> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<ArrayList<String>> brands) {
        this.brands = brands;
    }
}
