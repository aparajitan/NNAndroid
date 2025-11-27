package com.app_neighbrsnook.model.wall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.app_neighbrsnook.pojo.ImagePojo;

import java.util.List;

public class Listdatum{

    @SerializedName("type")
   
    private String type;
    @SerializedName("h_id")
   
    private String hId;
    @SerializedName("createdby")
   
    private String createdby;
    @SerializedName("neighborhood")
   
    private String neighborhood;
    @SerializedName("userpic")
   
    private String userpic;
    @SerializedName("username")
   
    private String username;

    @SerializedName("welcome_img")

    private String welcome_img;
    @SerializedName("category")

    private String category;
    @SerializedName("created_on")
   
    private String createdOn;
    @SerializedName("b_id")
   
    private String bId;

    @SerializedName("business_name")
    private String businessName;


    @SerializedName("sponsor_id")
    private String sponsor_id;

    @SerializedName("business_tagline")
   
    private String businessTagline;
    @SerializedName("business_desc")
   
    private String businessDesc;
    @SerializedName("business_status")
   
    private String businessStatus;
    @SerializedName("business_image")
   
    private List<ImagePojo> businessImage;
    @SerializedName("business_video")
   
    private List<Object> businessVideo;
    @SerializedName("review")
   
    private Integer review;
    @SerializedName("rating")
   
    private String rating;
    @SerializedName("postid")
   
    private String postid;
    @SerializedName("postlike")
   
    private String postlike;
    @SerializedName("totcomment")
   
    private String totcomment;
    @SerializedName("totallike")
   
    private String totallike;
    @SerializedName("groupid")
   
    private String groupid;
    @SerializedName("bannerid")
    private String bannerid;


    @SerializedName("pollid")
    private String pollid;
    @SerializedName("poll_title")
    private String pollTitle;
    @SerializedName("poll_question")
    
    private String pollQuestion;
    @SerializedName("poll_start_date")
    
    private String pollStartDate;
    @SerializedName("poll_end_date")
    
    private String pollEndDate;
    @SerializedName("isvoted")
    
    private String isvoted;
    @SerializedName("totalvote")
    
    private String totalvote;
    @SerializedName("willpollstart")
    
    private String willpollstart;
    @SerializedName("ispollrunning")
    
    private String ispollrunning;

    @SerializedName("eventid")

    private String eventid;

    @SerializedName("group_name")

    private String group_name;

    @SerializedName("event_description")

    private String event_description;


    @SerializedName("group_description")

    private String group_description;


    @SerializedName("event_name")

    private String event_name;

    @SerializedName("group_type")
    private String group_type;

    @SerializedName("post_message")
    private String post_message;

    @SerializedName("post_type")
    private String post_type;

    @SerializedName("post_images")
    private List<ImagePojo> postImages;

    private String status;
    @SerializedName("total_emojis")
    private Integer totalEmojis;

    @SerializedName("emojilistdata")
    private List<Emojilistdatum> emojilistdata;

    @SerializedName("emoji_status")
    private String  emoji_status;

    @SerializedName("user_emoji")
    private String  user_emoji;

    @SerializedName("favouritstatus")
    private int  favouritstatus;

    @SerializedName("caption")
    private String caption;

    @SerializedName("description")
    private String description;

    @SerializedName("company")
    private String company;

    @SerializedName("companylink")
    private String companylink;

    @SerializedName("externallink")
    private String externallink;

    @SerializedName("playstorelink")
    private String playstorelink;

    @SerializedName("companylogo")
    private String companylogo;

    @SerializedName("bannerimage")
    private String bannerimage;

    @SerializedName("event_start_date")
    private String event_start_date;

    @SerializedName("event_end_date")
    private String event_end_date;

    @SerializedName("getjoin")
    private String getjoin;

    @SerializedName("action")
    private String action;


    @SerializedName("welcome_msg")
    private String welcome_msg;

    public String getWelcomeid() {
        return welcomeid;
    }

    public void setWelcomeid(String welcomeid) {
        this.welcomeid = welcomeid;
    }

    @SerializedName("welcomeid")
    private String welcomeid;

    public String getPostemojiunicode() {
        return postemojiunicode;
    }

    public void setPostemojiunicode(String postemojiunicode) {
        this.postemojiunicode = postemojiunicode;
    }

    @SerializedName("postemojiunicode")
    private String  postemojiunicode;

    @SerializedName("group_image")
    @Expose
    private String groupImage;

    @SerializedName("event_cover_image")
    @Expose
    private String eventCoverImage;

    public String getEventCoverImage() {
        return eventCoverImage;
    }

    public void setEventCoverImage(String eventCoverImage) {
        this.eventCoverImage = eventCoverImage;
    }


    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    public String getUser_bokay() {
        return user_bokay;
    }

    public void setUser_bokay(String user_bokay) {
        this.user_bokay = user_bokay;
    }

    public String getTotal_like() {
        return total_like;
    }

    public void setTotal_like(String total_like) {
        this.total_like = total_like;
    }

    public String getTotal_bokay() {
        return total_bokay;
    }

    public void setTotal_bokay(String total_bokay) {
        this.total_bokay = total_bokay;
    }

    public List<LikeDataModel> getLikeDataModelList() {
        return likeDataModelList;
    }

    public void setLikeDataModelList(List<LikeDataModel> likeDataModelList) {
        this.likeDataModelList = likeDataModelList;
    }

    public List<BouquetDataModel> getBouquetDataModelList() {
        return bouquetDataModelList;
    }

    public void setBouquetDataModelList(List<BouquetDataModel> bouquetDataModelList) {
        this.bouquetDataModelList = bouquetDataModelList;
    }

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("like_status")
    private String like_status;

    @SerializedName("user_bokay")
    private String user_bokay;

    @SerializedName("total_like")
    private String total_like;

    @SerializedName("total_bokay")
    private String total_bokay;

    @SerializedName("likedata")
    private List<LikeDataModel> likeDataModelList;

    @SerializedName("bokaydata")
    private List<BouquetDataModel> bouquetDataModelList;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String gethId() {
        return hId;
    }

    public void sethId(String hId) {
        this.hId = hId;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessTagline() {
        return businessTagline;
    }

    public void setBusinessTagline(String businessTagline) {
        this.businessTagline = businessTagline;
    }

    public String getBusinessDesc() {
        return businessDesc;
    }

    public void setBusinessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public List<ImagePojo> getBusinessImage() {
        return businessImage;
    }

    public void setBusinessImage(List<ImagePojo> businessImage) {
        this.businessImage = businessImage;
    }

    public List<Object> getBusinessVideo() {
        return businessVideo;
    }

    public void setBusinessVideo(List<Object> businessVideo) {
        this.businessVideo = businessVideo;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostlike() {
        return postlike;
    }

    public void setPostlike(String postlike) {
        this.postlike = postlike;
    }

    public String getTotcomment() {
        return totcomment;
    }

    public void setTotcomment(String totcomment) {
        this.totcomment = totcomment;
    }

    public String getTotallike() {
        return totallike;
    }

    public void setTotallike(String totallike) {
        this.totallike = totallike;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getBannerid() {
        return bannerid;
    }

    public void setBannerid(String bannerid) {
        this.bannerid = bannerid;
    }

    public String getPollid() {
        return pollid;
    }

    public void setPollid(String pollid) {
        this.pollid = pollid;
    }

    public String getPollTitle() {
        return pollTitle;
    }

    public void setPollTitle(String pollTitle) {
        this.pollTitle = pollTitle;
    }

    public String getPollQuestion() {
        return pollQuestion;
    }

    public void setPollQuestion(String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    public String getPollStartDate() {
        return pollStartDate;
    }

    public void setPollStartDate(String pollStartDate) {
        this.pollStartDate = pollStartDate;
    }

    public String getPollEndDate() {
        return pollEndDate;
    }

    public void setPollEndDate(String pollEndDate) {
        this.pollEndDate = pollEndDate;
    }

    public String getIsvoted() {
        return isvoted;
    }

    public void setIsvoted(String isvoted) {
        this.isvoted = isvoted;
    }

    public String getTotalvote() {
        return totalvote;
    }

    public void setTotalvote(String totalvote) {
        this.totalvote = totalvote;
    }

    public String getWillpollstart() {
        return willpollstart;
    }

    public void setWillpollstart(String willpollstart) {
        this.willpollstart = willpollstart;
    }

    public String getIspollrunning() {
        return ispollrunning;
    }

    public void setIspollrunning(String ispollrunning) {
        this.ispollrunning = ispollrunning;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getGroup_description() {
        return group_description;
    }

    public void setGroup_description(String group_description) {
        this.group_description = group_description;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getGroup_type() {
        return group_type;
    }

    public void setGroup_type(String group_type) {
        this.group_type = group_type;
    }

    public String getPost_message() {
        return post_message;
    }

    public void setPost_message(String post_message) {
        this.post_message = post_message;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public List<ImagePojo> getPostImages() {
        return postImages;
    }

    public void setPostImages(List<ImagePojo> postImages) {
        this.postImages = postImages;
    }

    public Integer getTotalEmojis() {
        return totalEmojis;
    }

    public void setTotalEmojis(Integer totalEmojis) {
        this.totalEmojis = totalEmojis;
    }

    public List<Emojilistdatum> getEmojilistdata() {
        return emojilistdata;
    }

    public void setEmojilistdata(List<Emojilistdatum> emojilistdata) {
        this.emojilistdata = emojilistdata;
    }

    public String getEmoji_status() {
        return emoji_status;
    }

    public void setEmoji_status(String emoji_status) {
        this.emoji_status = emoji_status;
    }

    public String getUser_emoji() {
        return user_emoji;
    }

    public void setUser_emoji(String user_emoji) {
        this.user_emoji = user_emoji;
    }

    public int getFavouritstatus() {
        return favouritstatus;
    }

    public void setFavouritstatus(int favouritstatus) {
        this.favouritstatus = favouritstatus;
    }

    public String getWelcome_img() {
        return welcome_img;
    }

    public void setWelcome_img(String welcome_img) {
        this.welcome_img = welcome_img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanylink() {
        return companylink;
    }

    public void setCompanylink(String companylink) {
        this.companylink = companylink;
    }

    public String getExternallink() {
        return externallink;
    }

    public void setExternallink(String externallink) {
        this.externallink = externallink;
    }

    public String getPlaystorelink() {
        return playstorelink;
    }

    public void setPlaystorelink(String playstorelink) {
        this.playstorelink = playstorelink;
    }

    public String getCompanylogo() {
        return companylogo;
    }

    public void setCompanylogo(String companylogo) {
        this.companylogo = companylogo;
    }

    public String getBannerimage() {
        return bannerimage;
    }

    public void setBannerimage(String bannerimage) {
        this.bannerimage = bannerimage;
    }

    public String getEvent_start_date() {
        return event_start_date;
    }

    public void setEvent_start_date(String event_start_date) {
        this.event_start_date = event_start_date;
    }

    public String getEvent_end_date() {
        return event_end_date;
    }

    public void setEvent_end_date(String event_end_date) {
        this.event_end_date = event_end_date;
    }

    public String getGetjoin() {
        return getjoin;
    }

    public void setGetjoin(String getjoin) {
        this.getjoin = getjoin;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSponsor_id() {
        return sponsor_id;
    }

    public void setSponsor_id(String sponsor_id) {
        this.sponsor_id = sponsor_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWelcome_msg() {
        return welcome_msg;
    }

    public void setWelcome_msg(String welcome_msg) {
        this.welcome_msg = welcome_msg;
    }
}
