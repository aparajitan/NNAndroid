package com.app_neighbrsnook.model;

import com.google.gson.annotations.SerializedName;

public class OptionModel {
    @SerializedName("optionone")
    
    private String optionone;
    @SerializedName("optiontwo")
    
    private String optiontwo;
    @SerializedName("optionthree")
    
    private String optionthree;
    @SerializedName("optionfour")
    
    private String optionfour;

    public String getOptionone() {
        return optionone;
    }

    public void setOptionone(String optionone) {
        this.optionone = optionone;
    }

    public String getOptiontwo() {
        return optiontwo;
    }

    public void setOptiontwo(String optiontwo) {
        this.optiontwo = optiontwo;
    }

    public String getOptionthree() {
        return optionthree;
    }

    public void setOptionthree(String optionthree) {
        this.optionthree = optionthree;
    }

    public String getOptionfour() {
        return optionfour;
    }

    public void setOptionfour(String optionfour) {
        this.optionfour = optionfour;
    }
}
