package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

public class DocPojo {
    @SerializedName("doc")
    private String doc;

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }
}
