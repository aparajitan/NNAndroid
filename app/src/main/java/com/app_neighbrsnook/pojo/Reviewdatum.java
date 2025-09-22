package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

public class Reviewdatum {
    @SerializedName("username")
    private String username;
    @SerializedName("userpic")
    private String userpic;
    @SerializedName("review")
    private String review;
    @SerializedName("review_date")
    private String reviewDate;
    @SerializedName("neighbrhood")
    private String neighbrhood;
    @SerializedName("userid")
    private String userid;
    @SerializedName("id")
    private String id;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
