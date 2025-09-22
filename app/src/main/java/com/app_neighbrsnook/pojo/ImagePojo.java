package com.app_neighbrsnook.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagePojo implements Parcelable {

    @SerializedName("img")
    public String img;

    @SerializedName("video")
    public String video;

    @SerializedName("userid")
    @Expose
    private String userid;

    @SerializedName("imgid")
    @Expose
    private int imgid;

    @SerializedName("type")
    @Expose
    private String type;

    boolean isOwner;

    // Constructor
    public ImagePojo() {
    }

    // Parcelable constructor
    protected ImagePojo(Parcel in) {
        img = in.readString();
        video = in.readString();
        userid = in.readString();
        imgid = in.readInt();
        type = in.readString();
        isOwner = in.readByte() != 0;  // Read boolean as byte
    }

    public static final Creator<ImagePojo> CREATOR = new Creator<ImagePojo>() {
        @Override
        public ImagePojo createFromParcel(Parcel in) {
            return new ImagePojo(in);
        }

        @Override
        public ImagePojo[] newArray(int size) {
            return new ImagePojo[size];
        }
    };

    // Getters and Setters
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    // Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img);
        dest.writeString(video);
        dest.writeString(userid);
        dest.writeInt(imgid);
        dest.writeString(type);
        dest.writeByte((byte) (isOwner ? 1 : 0));  // Write boolean as byte
}
}