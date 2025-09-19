package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Annotation;
import java.util.List;

public class EventDetailsPojo implements SerializedName{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("images")
    @Expose
    private List<ImagePojo> images;
    @SerializedName("id")
    @Expose
    private String id;

    private String event_image_size;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("event_img_remain_limit")
    @Expose
    private String event_img_remain_limit;
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
    @SerializedName("iseventrunning")
    @Expose
    private String iseventrunning;


    @SerializedName("totcomment")
    @Expose
    private String totcomment;

    public String getTotcomment() {
        return totcomment;
    }

    public void setTotcomment(String totcomment) {
        this.totcomment = totcomment;
    }



    public String getFutureeventstatus() {
        return futureeventstatus;
    }

    public void setFutureeventstatus(String futureeventstatus) {
        this.futureeventstatus = futureeventstatus;
    }

    @SerializedName("futureeventstatus")
    @Expose
    private String futureeventstatus;


    @SerializedName("userunjoinmemberlist")
    @Expose
    private List<NonAttendeesPojoinDetails> userunjoinmemberlist;
    @SerializedName("userjoinmemberlist")
    @Expose
    private List<EventDetailsJoinMemberListPojo> userjoinmemberlist;

    public List<EventDetailsJoinMemberListPojo> getUserjoinmemberlist() {
        return userjoinmemberlist;
    }

    public void setUserjoinmemberlist(List<EventDetailsJoinMemberListPojo> userjoinmemberlist) {
        this.userjoinmemberlist = userjoinmemberlist;
    }

    @SerializedName("listdata")

    public List<EventDetailsUserListPojo> listdata;
    public List<EventDetailsUserListPojo> getListdata() {
        return listdata;
    }
    public void setListdata(List<EventDetailsUserListPojo> listdata) {
        this.listdata = listdata;
    }

    public List<NonAttendeesPojoinDetails> getUserunjoinmemberlist() {
        return userunjoinmemberlist;
    }

    public void setUserunjoinmemberlist(List<NonAttendeesPojoinDetails> userunjoinmemberlist) {
        this.userunjoinmemberlist = userunjoinmemberlist;
    }

    public String getStatus()  {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public List<ImagePojo> getImages() {
        return images;
    }

    public void setImages(List<ImagePojo> images) {
        this.images = images;
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



    public String getEvent_img_limit() {
        return event_img_limit;
    }

    public void setEvent_img_limit(String event_img_limit) {
        this.event_img_limit = event_img_limit;
    }

    private String event_img_limit;

    public String getEvent_image_size() {
        return event_image_size;
    }

    public void setEvent_image_size(String event_image_size) {
        this.event_image_size = event_image_size;
    }

    public void setNojoin(String nojoin) {
        this.nojoin = nojoin;
    }

    public String getIseventrunning() {
        return iseventrunning;
    }

    public void setIseventrunning(String iseventrunning) {
        this.iseventrunning = iseventrunning;
    }

    @Override
    public String value() {
        return null;
    }

    @Override
    public String[] alternate() {
        return new String[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
    public String getEvent_img_remain_limit() {
        return event_img_remain_limit;
    }

    public void setEvent_img_remain_limit(String event_img_remain_limit) {
        this.event_img_remain_limit = event_img_remain_limit;
    }

}
