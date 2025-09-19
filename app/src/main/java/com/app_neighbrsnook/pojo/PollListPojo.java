package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

public class PollListPojo {

    @SerializedName("p_id")
    private String pId;
//    @SerializedName("title")
//    private String title;
    @SerializedName("neighborhood")
    private String neighborhood;
    @SerializedName("poll_ques")
    private String pollQues;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("created_by")
    private String createdBy;
    @SerializedName("created_date")
    private String createdDate;
    @SerializedName("totalvote")
    private String totalvote;
    @SerializedName("userpic")
    private String userpic;

    @SerializedName("ispollrunning")
    private String ispollrunning;
    @SerializedName("isvoted")
    private String isvoted;

    @SerializedName("willpollstart")
    private String willpollstart;

    @SerializedName("userid")
    private String userid;

    @SerializedName("favouritstatus")
    private int favouritstatus;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getPollQues() {
        return pollQues;
    }

    public void setPollQues(String pollQues) {
        this.pollQues = pollQues;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getVote() {
        return totalvote;
    }

    public void setVote(String vote) {
        this.totalvote = vote;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getTotalvote() {
        return totalvote;
    }

    public void setTotalvote(String totalvote) {
        this.totalvote = totalvote;
    }

    public String getIspollrunning() {
        return ispollrunning;
    }

    public void setIspollrunning(String ispollrunning) {
        this.ispollrunning = ispollrunning;
    }


    public String getIsvoted() {
        return isvoted;
    }

    public void setIsvoted(String isvoted) {
        this.isvoted = isvoted;
    }

    public String getWillpollstart() {
        return willpollstart;
    }

    public void setWillpollstart(String willpollstart) {
        this.willpollstart = willpollstart;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getFavouritstatus() {
        return favouritstatus;
    }

    public void setFavouritstatus(int favouritstatus) {
        this.favouritstatus = favouritstatus;
    }
}
