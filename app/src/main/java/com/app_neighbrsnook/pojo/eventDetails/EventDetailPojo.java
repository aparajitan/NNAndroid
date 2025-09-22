package com.app_neighbrsnook.pojo.eventDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventDetailPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("images")
    @Expose
    private List<Image> images;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("event_start_date")
    @Expose
    private String eventStartDate;
    @SerializedName("event_end_date")
    @Expose
    private String eventEndDate;
    @SerializedName("event_starttime")
    @Expose
    private String eventStarttime;
    @SerializedName("event_endtime")
    @Expose
    private String eventEndtime;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;
    @SerializedName("event_detail")
    @Expose
    private String eventDetail;
    @SerializedName("addlineone")
    @Expose
    private String addlineone;
    @SerializedName("addlinetwo")
    @Expose
    private String addlinetwo;
    @SerializedName("userattends")
    @Expose
    private String userattends;
    @SerializedName("userjoinmemberlist")
    @Expose
    private List<Userjoinmember> userjoinmemberlist;
    @SerializedName("userunjoinmemberlist")
    @Expose
    private List<Object> userunjoinmemberlist;
    @SerializedName("userlikes")
    @Expose
    private String userlikes;
    @SerializedName("datetimeandneighbrhood")
    @Expose
    private String datetimeandneighbrhood;
    @SerializedName("createby")
    @Expose
    private String createby;
    @SerializedName("userpic")
    @Expose
    private String userpic;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("total_like")
    @Expose
    private String totalLike;
    @SerializedName("total_join")
    @Expose
    private String totalJoin;
    @SerializedName("unjoin")
    @Expose
    private String unjoin;
    @SerializedName("nojoin")
    @Expose
    private String nojoin;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventStarttime() {
        return eventStarttime;
    }

    public void setEventStarttime(String eventStarttime) {
        this.eventStarttime = eventStarttime;
    }

    public String getEventEndtime() {
        return eventEndtime;
    }

    public void setEventEndtime(String eventEndtime) {
        this.eventEndtime = eventEndtime;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String getAddlineone() {
        return addlineone;
    }

    public void setAddlineone(String addlineone) {
        this.addlineone = addlineone;
    }

    public String getAddlinetwo() {
        return addlinetwo;
    }

    public void setAddlinetwo(String addlinetwo) {
        this.addlinetwo = addlinetwo;
    }

    public String getUserattends() {
        return userattends;
    }

    public void setUserattends(String userattends) {
        this.userattends = userattends;
    }

    public List<Userjoinmember> getUserjoinmemberlist() {
        return userjoinmemberlist;
    }

    public void setUserjoinmemberlist(List<Userjoinmember> userjoinmemberlist) {
        this.userjoinmemberlist = userjoinmemberlist;
    }

    public List<Object> getUserunjoinmemberlist() {
        return userunjoinmemberlist;
    }

    public void setUserunjoinmemberlist(List<Object> userunjoinmemberlist) {
        this.userunjoinmemberlist = userunjoinmemberlist;
    }

    public String getUserlikes() {
        return userlikes;
    }

    public void setUserlikes(String userlikes) {
        this.userlikes = userlikes;
    }

    public String getDatetimeandneighbrhood() {
        return datetimeandneighbrhood;
    }

    public void setDatetimeandneighbrhood(String datetimeandneighbrhood) {
        this.datetimeandneighbrhood = datetimeandneighbrhood;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }

    public String getTotalJoin() {
        return totalJoin;
    }

    public void setTotalJoin(String totalJoin) {
        this.totalJoin = totalJoin;
    }

    public String getUnjoin() {
        return unjoin;
    }

    public void setUnjoin(String unjoin) {
        this.unjoin = unjoin;
    }

    public String getNojoin() {
        return nojoin;
    }

    public void setNojoin(String nojoin) {
        this.nojoin = nojoin;
    }

}
