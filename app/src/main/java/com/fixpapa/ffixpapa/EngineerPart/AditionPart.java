package com.fixpapa.ffixpapa.EngineerPart;

import java.io.Serializable;

public class AditionPart implements Serializable {


    private String partName;

    private String partNumber;

    private String cost;


    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }


}
