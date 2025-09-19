package com.app_neighbrsnook.model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class ImagePOJO implements Serializable {
    public Bitmap bitmap;
    public Uri imageUri;
    public Uri videoUri;
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}