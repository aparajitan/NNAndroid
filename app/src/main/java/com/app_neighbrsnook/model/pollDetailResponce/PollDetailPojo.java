package com.app_neighbrsnook.model.pollDetailResponce;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PollDetailPojo {
    @SerializedName("status")
    
    private String status;
    @SerializedName("message")
    
    private String message;
    @SerializedName("p_id")
    
    private String pId;
    @SerializedName("title")
    
    private String title;
    @SerializedName("neighborhood")
    
    private String neighborhood;
    @SerializedName("poll_ques")
    
    private String pollQues;
    @SerializedName("start_date")
    
    private String startDate;
    @SerializedName("end_date")
    
    private String endDate;
    @SerializedName("comp_enddate")
    
    private String compEnddate;
    @SerializedName("created_by")
    
    private String createdBy;
    @SerializedName("created_date")
    
    private String createdDate;

    @SerializedName("options")
    private List<Option> options;

    @SerializedName("userid")
    
    private String userid;
    @SerializedName("total")
    
    private String total;
    @SerializedName("userget")
    
    private String userget;
    @SerializedName("vote")
    
    private String vote;
    @SerializedName("userpic")
    
    private String userpic;

    @SerializedName("isvoted")

    private String isvoted;
    @SerializedName("poll_running_status")

    private String pollRunningStatus;

    @SerializedName("willpollstart")
    private String willpollstart;

    public String getEdit_poll_status() {
        return edit_poll_status;
    }

    public void setEdit_poll_status(String edit_poll_status) {
        this.edit_poll_status = edit_poll_status;
    }

    @SerializedName("edit_poll_status")
    private String edit_poll_status;

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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getCompEnddate() {
        return compEnddate;
    }

    public void setCompEnddate(String compEnddate) {
        this.compEnddate = compEnddate;
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

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUserget() {
        return userget;
    }

    public void setUserget(String userget) {
        this.userget = userget;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getIsvoted() {
        return isvoted;
    }

    public void setIsvoted(String isvoted) {
        this.isvoted = isvoted;
    }

    public String getPollRunningStatus() {
        return pollRunningStatus;
    }

    public void setPollRunningStatus(String pollRunningStatus) {
        this.pollRunningStatus = pollRunningStatus;
    }

    public String getWillpollstart() {
        return willpollstart;
    }

    public void setWillpollstart(String willpollstart) {
        this.willpollstart = willpollstart;
    }
}
