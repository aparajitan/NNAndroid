package com.app_neighbrsnook.pollModule;

import com.google.gson.annotations.SerializedName;

public class PercentageModel {
    @SerializedName("percentage1")
    
    private Integer percentage1;
    @SerializedName("percentage2")
    
    private Integer percentage2;
    @SerializedName("percentage3")
    
    private Integer percentage3;
    @SerializedName("percentage4")
    
    private Integer percentage4;


    public Integer getPercentage1() {
        return percentage1;
    }

    public void setPercentage1(Integer percentage1) {
        this.percentage1 = percentage1;
    }

    public Integer getPercentage2() {
        return percentage2;
    }

    public void setPercentage2(Integer percentage2) {
        this.percentage2 = percentage2;
    }

    public Integer getPercentage3() {
        return percentage3;
    }

    public void setPercentage3(Integer percentage3) {
        this.percentage3 = percentage3;
    }

    public Integer getPercentage4() {
        return percentage4;
    }

    public void setPercentage4(Integer percentage4) {
        this.percentage4 = percentage4;
    }
}
