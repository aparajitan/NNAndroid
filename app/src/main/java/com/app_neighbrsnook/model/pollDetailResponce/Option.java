package com.app_neighbrsnook.model.pollDetailResponce;

import com.google.gson.annotations.SerializedName;

public class Option {
    @SerializedName("option")
    
    private String option;
    @SerializedName("percentage")
    
    private String percentage;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
