package com.app_neighbrsnook.network;

import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.model.postComment.CommentLikePojo;
import com.app_neighbrsnook.model.wall.PostEmojiListModel;
import com.app_neighbrsnook.model.wall.WelcomeEmojiListModel;
import com.app_neighbrsnook.pojo.EmailPojoStatus;
import com.app_neighbrsnook.pojo.OtpVerifyCheckPojo;
import com.app_neighbrsnook.pojo.RegisterationOtpPojo;
import com.app_neighbrsnook.pojo.UploadDocumentResponse;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.pojo.marketPlacePojo.DetailsPojoMarketPlaceParent;
import com.app_neighbrsnook.pojo.marketPlacePojo.MarketPlaceSendMsgPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.MarketPlaceWallPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.UsersListChatsPojo;
import com.google.gson.JsonElement;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.model.AppLimitPojo;
import com.app_neighbrsnook.model.BusinessModel1;
import com.app_neighbrsnook.model.SubsModel;
import com.app_neighbrsnook.model.faq.Faq;
import com.app_neighbrsnook.model.groupChat.GroupChatPojo;
import com.app_neighbrsnook.model.neighbrhood.NearbyModel;
import com.app_neighbrsnook.model.notification.NotificationModel;
import com.app_neighbrsnook.model.pollDetailResponce.PollDetailPojo;
import com.app_neighbrsnook.model.postComment.CommentPojo;
import com.app_neighbrsnook.model.publicdirectory.PublicDirectoryPojo;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.pojo.BusinessDetailPojo;
import com.app_neighbrsnook.pojo.BusinessListPojo;
import com.app_neighbrsnook.pojo.EmojiPojo;
import com.app_neighbrsnook.pojo.EventDetailsPojo;
import com.app_neighbrsnook.pojo.EventLikesStatusPojo;
import com.app_neighbrsnook.pojo.EventListPojos;
import com.app_neighbrsnook.pojo.GroupDetailsPojo;
import com.app_neighbrsnook.pojo.GroupDetailsbyNameResponse;
import com.app_neighbrsnook.pojo.GroupResponseListPojo;
import com.app_neighbrsnook.pojo.JoinEventUserListPojo;
import com.app_neighbrsnook.pojo.Poll;
import com.app_neighbrsnook.pojo.PostPojo;
import com.app_neighbrsnook.pojo.RateNowPojo;
import com.app_neighbrsnook.pojo.ReviewPojo;
import com.app_neighbrsnook.pojo.SettingPojo;
import com.app_neighbrsnook.pojo.StatePojo;
import com.app_neighbrsnook.pojo.directmessagelist.DirectMessagePojo;
import com.app_neighbrsnook.pojo.dm.DmModel;
import com.app_neighbrsnook.pojo.neighborhoodMember.MemberPojo;
import com.app_neighbrsnook.pojo.reportModule.ReportTypePojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @POST("business")
    @FormUrlEncoded
    Call<BusinessModel1> createBusiness(@Query("flag") String flag,
                                      @FieldMap HashMap<String, Object> partMap);


//
@POST("business")
@Multipart
    Call<BusinessModel1> createBusiness1(@Query("flag") String flag,
                                         @Part("businessname") RequestBody businessname,
                                         @Part("tagline")  RequestBody tagline,
                                         @Part("cat") RequestBody cat,
                                         @Part("description")  RequestBody description,
                                         @Part("opentime")  RequestBody opentime,
                                         @Part("closetime") RequestBody closetime,
                                         @Part("weekoff")  RequestBody weekoff,
                                         @Part("address1") RequestBody address1,
                                         @Part("address2")  RequestBody address2,

                                         @Part("pin") RequestBody pin,
                                         @Part("web")  RequestBody web,
                                         @Part("email")  RequestBody email,
                                         @Part("mobile")  RequestBody mobile,
                                         @Part("telephone")  RequestBody telephone,
                                         @Part("userid")  RequestBody userid,
                                         @Part("doctype")  RequestBody doctype,
                                         @Part List<MultipartBody.Part>  file,
                                         @Part List<MultipartBody.Part> doc,
                                         @Header("Accept") String acceptHeader
                                        );



    @POST("business")
    @Multipart
    Call<BusinessModel1> updateBusiness(@Query("flag") String flag,
                                         @Part("businessname") RequestBody businessname,
                                         @Part("tagline")  RequestBody tagline,
                                         @Part("cat") RequestBody cat,
                                         @Part("description")  RequestBody description,
                                         @Part("opentime")  RequestBody opentime,
                                         @Part("closetime") RequestBody closetime,
                                         @Part("weekoff")  RequestBody weekoff,
                                         @Part("address1") RequestBody address1,
                                         @Part("address2")  RequestBody address2,
                                         @Part("pin") RequestBody pin,
                                         @Part("web")  RequestBody web,
                                         @Part("email")  RequestBody email,
                                         @Part("mobile")  RequestBody mobile,
                                         @Part("telephone")  RequestBody telephone,
                                         @Part("userid")  RequestBody userid,
                                         @Part("doctype")  RequestBody doctype,
                                        @Part("businessid")  RequestBody businessid,
                                         @Part List<MultipartBody.Part>  file,
                                         @Part List<MultipartBody.Part> doc,
                                        @Header("Accept") String acceptHeader

    );
//    @Part("field1") @FormUrlEncoded String field1,
//    @Part("field2") @FormUrlEncoded String field2,
//    @Part MultipartBody.Part filePart
    @POST("business")
    @FormUrlEncoded
    Call<BusinessModel1> editBusiness(@Query("flag") String flag,
                                         @FieldMap HashMap<String,Object> params);

    @POST("business")
    @FormUrlEncoded
    Call<JsonElement> deleteBusiness(@Query("flag") String flag,
                                     @FieldMap HashMap<String, Object> hm);


    @POST("groups")
    @FormUrlEncoded
    Call<JsonElement> deleteGroup(@Query("flag") String flag,
                                     @FieldMap HashMap<String, Object> hm);

    @POST("posting")
    @FormUrlEncoded
    Call<CommentPojo> commentDeleteApi(@Query("flag") String commentDelete,
                                       @FieldMap HashMap<String, Object>hm);
    @POST("master")
    @FormUrlEncoded
    Call<JsonElement> deleteProfileImage(@Query("flag") String flag,
                                     @FieldMap HashMap<String, Object> hm);
    @POST("groups")
    @FormUrlEncoded
    Call<JsonElement> userDeleteinGroup(@Query("flag") String flag,
                                     @FieldMap HashMap<String, Object> hm);
    @POST("groups")
    @FormUrlEncoded
    Call<JsonElement> userExit(@Query("flag") String flag,
                                     @FieldMap HashMap<String, Object> hm);

    @POST("groups")
    @FormUrlEncoded
    Call<AddressResponse> userJoinGroup(@Query("flag") String flag,
                                     @FieldMap HashMap<String, Object> hm);


    @POST("groups")
    @FormUrlEncoded
    Call<JsonElement> approveDeclined(@Query("flag") String flag,
                                     @FieldMap HashMap<String, Object> hm);


    @POST("event")
    @FormUrlEncoded
    Call<JsonElement> imageDelete(@Query("flag") String flag,
                                     @FieldMap HashMap<String, Object> hm);

    @POST("master")
    @FormUrlEncoded
    Call<ReviewPojo> deviceInfoApi(@Query("flag") String flag,
                                   @FieldMap HashMap<String,Object> params);

    @POST("master")
    @FormUrlEncoded
    Call<ReviewPojo> getPopupVerified(@Query("flag") String flag, @FieldMap HashMap<String,Object> params);

    @POST("business")
    @FormUrlEncoded
    Call<BusinessListPojo> businesslist(@Query("flag") String businesslist,
                                        @FieldMap HashMap<String, Object> hm);

    @POST("groups")
    @FormUrlEncoded
    Call<GroupResponseListPojo> groupList(@Query("flag") String groupList,
                                          @FieldMap HashMap<String, Object> hm);

    @POST("otpverification")
    @FormUrlEncoded
    Call<OtpVerifyCheckPojo> checkOtp(@Query("flag") String checkverify,
                                      //  @Query("reqestmobileno") int mobile,

                                      @FieldMap HashMap<String, String> hm);



    @POST("event")
    @FormUrlEncoded
    Call<JsonElement> deleteEvent(@Query("flag") String flag,
                                  @FieldMap HashMap<String, Object> hm);
    @POST("poll")
    @FormUrlEncoded
    Call<JsonElement> deletePoll(@Query("flag") String flag,
                                  @FieldMap HashMap<String, Object> hm);



    @POST("event")
    @FormUrlEncoded
    Call<EventListPojos> eventlistall(@Query("flag") String groupList,
                                      // @Query("eventdate")String eventdate,
                                      @FieldMap HashMap<String, Object>hm);


    @POST("welcome")
    @FormUrlEncoded
    Call<EmojiPojo>thumsUpEmojiApi(@Query("flag")String subs,
                                            @FieldMap HashMap<String, Object>hm);


    @POST("event")
    @FormUrlEncoded
    Call<JsonElement> eventJoinRequest(@Query("flag") String groupList,
                                             @FieldMap HashMap<String, Object>hm);

    @POST("groups")
    Call<GroupDetailsbyNameResponse> groupbyNameList(@Query("flag") String groupList,
                                               @Query("groupid") String groupid);


    @POST("business")
    @FormUrlEncoded
    Call<CommentPojo> reviewDeleteApi(@Query("flag") String reviewDelete,
                                      @FieldMap HashMap<String, Object>hm);

    @POST("chat")
    @FormUrlEncoded
    Call<DmModel> dmDeleteApi(@Query("flag") String dmDelete,
                              @FieldMap HashMap<String, Object>hm);




    @POST("event")
    @FormUrlEncoded
    Call<EventDetailsPojo> eventDetails(@Query("flag") String flag,
                                        //  @Query("eventid")int eventid,
                                        @FieldMap HashMap<String, Object>hm);

    @POST("posting")
    @FormUrlEncoded
    Call<com.app_neighbrsnook.model.post.PostPojo> getPostDetail(@Query("flag") String flag,
                                                                 @FieldMap HashMap<String, Object>hm);

    @POST("event")
    @FormUrlEncoded
    Call<JoinEventUserListPojo> eventUserListDetails (@Query("flag") String eventuserlist,
                                                      //@Query("eventid") String eventid);
                                                      @FieldMap HashMap<String, Object>hm);

    @POST("posting")
    @FormUrlEncoded
    Call<CommentLikePojo> commentLikeApi(@Query("flag") String likeComment,
                                         @FieldMap HashMap<String,Object>hm);
    @POST("event")
    @FormUrlEncoded
    Call<EventLikesStatusPojo> eventLikesUserLists (@Query("flag") String eventuserlist,
                                                    //@Query("eventid") String eventid);
                                                    @FieldMap HashMap<String, Object>hm);


    @POST("business")
    @FormUrlEncoded
    Call<BusinessDetailPojo> businessDetail(@Query("flag") String businesslist,
                                            @FieldMap HashMap<String, Object>hm);

    @POST("groups")
    @FormUrlEncoded
    Call<GroupDetailsPojo> groupDetails(@Query("flag") String groupList,
                                        @FieldMap HashMap<String, Object>hm);

    @POST("groups")
    @FormUrlEncoded
    Call<GroupDetailsPojo> groupupdateShow(@Query("flag") String groupList,
                                        @FieldMap HashMap<String, Object>hm);

    @POST("mobilesettings")
    @FormUrlEncoded
    Call<SettingPojo> settingData(@Query("flag") String groupList,
                                  @FieldMap HashMap<String, Object>hm);




    @POST("master")
    Call<StatePojo> stateList(@Query("flag") String master,
                              @Query("countryid") List<Integer> list);

    @POST("master")
    Call<StatePojo> city(@Query("flag") String master,
                              @Query("stateid") List<Integer> list);

    @POST("master")
    Call<StatePojo> countryList(@Query("flag") String master);

    @GET("business")
    Call<StatePojo> businessCategoryList(@Query("flag") String master);

    @GET("master")
    Call<StatePojo> skip2CategoryApi(@Query("flag") String master);

    @GET("api/category")
    Call<StatePojo> createCategoryProducr();

    @GET("api/mpk_home_wall")
    Call<WallPojo> marketPlaceWall(@Query("user_id") int userid);

    @GET("api/mpk_product_home")
    Call<MarketPlaceWallPojo> marketPlaceFilterfilters(@Query("user_id") int userid,
                                       @Query("filters") int filters);

    @GET("api/mpk_product_home")
    Call<WallPojo> marketPlaceAllList(@Query("user_id") int userid);

    @GET("api/mpk_product_detail")
    Call<DetailsPojoMarketPlaceParent> marketPlaceDetailsApi(@Query("product_id") int product_id,
                                                             @Query("user_id") int userid);

    @GET("api/mpk_product_detail")
    Call<DetailsPojoMarketPlaceParent> marketPlaceDetailsApiEdit(@Query("product_id") int product_id);



    @POST("posting")
    @FormUrlEncoded
    Call<PostEmojiListModel> postCommentLikeListApi(@Query("flag") String flag,
                                                    @FieldMap HashMap<String,Object>hm);

    @GET("api/mpk_product_list")
    Call<WallPojo> myProductList(@Query("user_id") int userid);


    @GET("api/wishlist/{user_id}")
    Call<WallPojo> myWishList(@Path("user_id") int userId);

    @GET("api/mpk_product_today_list")
    Call<WallPojo> todayList(@Query("user_id") int userid);

    @POST("api/wishlist")
    @FormUrlEncoded
    Call<CommonPojoSuccess> addWishList(
            @FieldMap HashMap<String,Object> hm);

  @DELETE("api/wishlist")
   // @FormUrlEncoded
    Call<CommonPojoSuccess> dltWishlist();
           // @FieldMap HashMap<String,Object> hm);

  @PUT("api/chat_read_status")
    Call<CommonPojoSuccess> checkStatusUpdate(
            @Body HashMap<String,Object> hm);



    @GET("api/seller-chat-list")
    Call<UsersListChatsPojo> chatUserMarket(@Query("user_id") int product_id);


    @PUT("api/product_inactive_status")
    @FormUrlEncoded
    Call<CommonPojoSuccess> inActiveApi(
            @FieldMap HashMap<String,Object> hm);


    @PUT("api/product_active_status")
    @FormUrlEncoded
    Call<CommonPojoSuccess> activeStatus(
            @FieldMap HashMap<String,Object> hm);


    @POST("api/user-location")
    Call<ResponseBody> neighLocationStatus(@Body HashMap<String, Object> map);

    @POST("api/verify-email")
    @FormUrlEncoded
    Call<EmailPojoStatus> emailVerify(
            @FieldMap HashMap<String,Object> hashMap);

    @PUT("api/product_sold_status")
    @FormUrlEncoded
    Call<CommonPojoSuccess> productSold(
            @FieldMap HashMap<String,Object> hm);

    @PUT("api/product_delete_inactive_status")
    @FormUrlEncoded
    Call<CommonPojoSuccess> deleteProductApi(
            @FieldMap HashMap<String,Object> hm);



    @GET("api/category")
    Call<WallPojo> categoryList();

    @GET("master")
    Call<StatePojo> skip2ChooseInteres(@Query("flag") String master);

    @GET("master")
    Call<StatePojo> skip2loveNeighbrhoodset(@Query("flag") String master);

    @GET("master")
    Call<StatePojo> savedNeighbrhood(@Query("flag") String master);


    @POST("business")
    @FormUrlEncoded
    Call<RateNowPojo> businessRating(@Query("flag") String businesslist,
                                     @FieldMap HashMap<String, Object> hm);
    @POST("business")
    @FormUrlEncoded
    Call<RateNowPojo> writeReview(@Query("flag")String businessreview,
                                  @FieldMap HashMap<String, Object> hm);
    @POST("business")
    @FormUrlEncoded
    Call<ReviewPojo> listReview(@Query("flag")String allreviewlist,
                                @FieldMap HashMap<String, Object> hm);

    @POST("poll")
    @FormUrlEncoded
    Call<JsonElement> createPoll(@Query("flag")String poll,
                          @FieldMap HashMap<String, Object> hm);
    @POST("poll")
    @FormUrlEncoded
    Call<Poll> pollList(@Query("flag")String poll,
                        @FieldMap HashMap<String, Object> hm);

    @POST("poll")
    @FormUrlEncoded
    Call<PollDetailPojo> pollLDetail(@Query("flag")String poll,
                                     @FieldMap HashMap<String, Object>hm);

    @POST("poll")
    @FormUrlEncoded
    Call<JsonElement> pollLVote(@Query("flag")String poll,
                                     @FieldMap HashMap<String, Object>hm);

    @POST("master")
    @FormUrlEncoded
    Call<ReviewPojo> skipApi(@Query("flag") String flag,
                                        @FieldMap HashMap<String,Object> params);

    @POST("master")
    @FormUrlEncoded
    Call<AddressResponse> skipLittleMoreApi(@Query("flag") String flag,
                                         @FieldMap HashMap<String,Object> hm);
    @POST("master")
    @FormUrlEncoded
    Call<AddressResponse> changePassword(@Query("flag") String flag,
                                         @FieldMap HashMap<String,Object> hm);

    @POST("master")
    @FormUrlEncoded
    Call<JsonElement> profileapisec(@Query("flag") String flag,
                                       @Query("userid") int userid,
                                       @FieldMap HashMap<String,Object> hm);
    @POST("master")
    @FormUrlEncoded
    Call<GroupResponseListPojo> uploadDocuments(@Query("flag") String flag,
                                       @FieldMap HashMap<String,Object> hm);
    @POST("master")
    @FormUrlEncoded
    Call<UploadDocumentResponse> documentUserView(@Query("flag") String flag,
                                                  //@Query("userid") int userid,
                                                  @FieldMap HashMap<String,Object> hm);


    @POST("master")
    @FormUrlEncoded

    Call<JsonElement> otherProfileview(@Query("flag") String flag,
                                        @Query("userid") int userid,
    @FieldMap HashMap<String,Object> hm);


    @POST("mobilesettings")
    Call<JsonElement> settingDataApi(@Query("flag") String flag,
                                        @Query("userid") int userid);


    @POST("posting")
    @FormUrlEncoded
    Call<WallPojo> wallListApi(@Query("flag") String businesslist,
                                @FieldMap HashMap<String, Object>hm);


    @POST("posting")
    @FormUrlEncoded
    Call<WallPojo> favistApi(@Query("flag") String businesslist,
                               @FieldMap HashMap<String, Object>hm);

    @POST("posting")
    @FormUrlEncoded
    Call<WallPojo> commentApi(@Query("flag") String businesslist,
                               @FieldMap HashMap<String, Object>hm);

    @POST("event")
    @FormUrlEncoded
    Call<AddressResponse> eventCommnentApii(@Query("flag") String eventcomment,
                               @FieldMap HashMap<String, Object>hm);

    @POST("posting")
    @FormUrlEncoded
    Call<CommentPojo> commentListApi(@Query("flag") String comment,
                                     @FieldMap HashMap<String, Object>hm);

    @POST("event")
    @FormUrlEncoded
    Call<CommentPojo> eventListApi(@Query("flag") String comment,
                                     @FieldMap HashMap<String, Object>hm);

    @POST("all-notification")
    @FormUrlEncoded
    Call<NotificationModel> notificationListApi(@Query("flag") String businesslist,
                                                @Query("appkey") String appkey,
                                                @FieldMap HashMap<String, Object>hm);

    @POST("hide-notification")
    @FormUrlEncoded
    Call<NotificationModel> hideNotificationApi(@Query("flag") String businesslist,
                                                @Query("appkey") String appkey,
                                                @FieldMap HashMap<String, Object>hm);


    @GET("master")
    Call<Faq> faqApi(@Query("flag") String flag);


    @POST("master")
    @FormUrlEncoded
    Call<BusinessListPojo> searchApi(@Query("flag") String flag,
                                            @Query("appkey") String appkey,
                                            @FieldMap HashMap<String,Object> hm);

    @POST("myneighborhood")
    @FormUrlEncoded
    Call<NearbyModel> myneighborhood(@Query("flag") String businesslist,
                                     @FieldMap HashMap<String, Object>hm);

    @POST("neighbrsnook")
    @FormUrlEncoded
    Call<SubsModel> neighborhoodSubscribeApi(@Query("flag") String subs,
                                             @FieldMap HashMap<String, Object>hm);

    @POST("chat")
    @FormUrlEncoded
    Call<DmModel> showChatApi(@Query("flag") String subs,
                              @FieldMap HashMap<String, Object>hm);

    @POST("chat")
    @FormUrlEncoded
    Call<DmModel> sendMsgApi(@Query("flag") String subs,
                              @FieldMap HashMap<String, Object>hm);

    @POST("api/send-message")
    @FormUrlEncoded
    Call<MarketPlaceSendMsgPojo> sendMsgMarketPlaceApi(
            @FieldMap HashMap<String, Object>hm);

    @POST("chat")
    @FormUrlEncoded
    Call<DirectMessagePojo> directMsgListApi(@Query("flag") String subs,
                                   @FieldMap HashMap<String, Object>hm);

    @POST("chat")
    @FormUrlEncoded
    Call<MemberPojo> memberListApi(@Query("flag") String subs,
                                   @FieldMap HashMap<String, Object>hm);


    @POST("posting")
    Call<StatePojo> postTpypeList(@Query("flag") String master);

    @POST("posting")
    @FormUrlEncoded
    Call<PostPojo> createWallPost (@Query("flag") String subs,
                                   @FieldMap HashMap<String, Object>hm);


    @Multipart
    @POST("posting")
    Call<PostPojo> uploadData(
            @Query("flag") String subs,
            @Part("userid") RequestBody userId,
            @Part("posttype") RequestBody postType,
            @Part("postmsg") RequestBody postMsg,
            @Part List<MultipartBody.Part>  file
    );

    @Multipart
    @POST("event")
    Call<AddressResponse> uploadImageEvent(
            @Query("flag") String subs,
            @Part("userid") RequestBody userId,
            @Part("event_id") RequestBody evenid,
            @Part List<MultipartBody.Part> file
    );


    @POST("api/mpk_product_add")
    @Multipart
    Call<AddressResponse> uploadPRoduct(
            @Part("p_title") RequestBody p_title,
            @Part("p_description") RequestBody p_description,
            @Part("sale_type") RequestBody sale_type,
            @Part("sale_price") RequestBody sale_price,
            @Part("brand_name") RequestBody brand_name,
            @Part("seller_name") RequestBody seller_name,
            @Part("cat_id") RequestBody cat_id,
            @Part("p_images[]") RequestBody p_image,
            @Part("p_status") RequestBody p_status,
            @Part("neighborhood_id") RequestBody neighborhood_id,
            @Part("created_by") RequestBody created_by,
            @Part List<MultipartBody.Part>  list
    );

    @Multipart
    @POST("posting")
    Call<ResponseBody> getUploadReferrerFile( @Query("flag") String subs,
                                              @Part("userid") RequestBody userId,
                                              @Part("posttype") RequestBody postType,
                                              @Part("postmsg") RequestBody postMsg,
                                              @Part List<MultipartBody.Part> image);


    @POST("contactus")
    @FormUrlEncoded
    Call<StatePojo> contactUs (@Query("flag") String subs,
                                   @FieldMap HashMap<String, Object>hm);

    @POST("aboutusapi")
    Call<StatePojo> aboutusapi(@Query("flag") String subs,
                               @Query("userid") int userid,
                               @Query("appkey") String appkey
                               );

    @POST("posting")
    @FormUrlEncoded
    Call<EmojiPojo> emojiApi(@Query("flag") String subs,
                             @FieldMap HashMap<String, Object>hm);

    @POST("posting")
    @FormUrlEncoded
    Call<EmojiPojo> deletePostApi(@Query("flag") String subs,
                             @FieldMap HashMap<String, Object>hm);

    @POST("master")
    @FormUrlEncoded
    Call<PublicDirectoryPojo> publicDirApi(@Query("flag") String subs,
                                       @FieldMap HashMap<String, Object>hm);
    @POST("posting")
    @FormUrlEncoded
    Call<WallPojo> favApi(@Query("flag") String subs,
                          @FieldMap HashMap<String, Object>hm);

    @POST("groups")
    @FormUrlEncoded
    Call<GroupChatPojo> sendGroupMsgApi(@Query("flag") String subs,
                                        @FieldMap HashMap<String, Object>hm);
    @POST("groups")
    @FormUrlEncoded
    Call<GroupChatPojo> showGroupChatApi(@Query("flag") String subs,
                                         @FieldMap HashMap<String, Object>hm);
    @POST("notificationcount")
    @FormUrlEncoded
    Call<StatePojo> notificationCountApi(@Query("flag") String flag,
                                              @Query("appkey") String appkey,
                                         @FieldMap HashMap<String, Object>hm);

    @POST("notificationcount")
    @FormUrlEncoded
    Call<StatePojo> notificationStatusChangeApi(@Query("flag") String flag,
                                         @Query("appkey") String appkey,
                                         @FieldMap HashMap<String, Object>hm);

    @POST("reportapp")
    Call<ReportTypePojo> reportTypeApi(@Query("flag") String flag);

    @POST("reportapp")
    @FormUrlEncoded
    Call<ReportTypePojo> reportApi(@Query("flag") String flag,
                                   @FieldMap HashMap<String, Object>hm);

//    Call<EmojiPojo> sponserClickApi(String sponsorevent, HashMap<String, Object> hm);

    @POST("sponsor")
    @FormUrlEncoded
    Call<JsonElement> sponserClickApi(@Query("flag")String poll,
                                @FieldMap HashMap<String, Object>hm);
    @POST("mobilesettings")
    Call<AppLimitPojo> appImageSettingApi(@Query("flag")String poll);

    @POST("posting")
    @FormUrlEncoded
    Call<com.app_neighbrsnook.model.post.PostPojo> PostPojoList(@Query("flag") String flag,
                                                                @FieldMap HashMap<String, Object>hm);

    @GET("master")
    Call<StatePojo> mailApiSkip(@Query("flag") String master);

    @POST("welcome")
    @FormUrlEncoded
    Call<WelcomeEmojiListModel> welEmojiListApi(@Query("flag") String flag,
                                                @FieldMap HashMap<String, Object>hm);

    @POST("posting")
    @FormUrlEncoded
    Call<PostEmojiListModel> postEmojiListApi(@Query("flag") String flag,
                                              @FieldMap HashMap<String,Object>hm);



    @FormUrlEncoded
    @POST(UrlClass.OTP) 
    Call<RegisterationOtpPojo> sendOtp(@Field("reqestmobileno") String phoneNumber);

    @FormUrlEncoded
    @POST(UrlClass.OTP_FORGOT)
    Call<RegisterationOtpPojo> forgtoOtp(@Field("reqestmobileno") String phoneNumber);

    @FormUrlEncoded
    @POST("master?flag=createuser")
    Call<ResponseBody> signup(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("emailid") String emailid,
            @Field("phoneno") String phoneno,
            @Field("password") String password,
            @Field("confirm_password") String confirmPassword,
            @Field("term") String term,
            @Field("firebase_token") String token
    );
    @Multipart
    @POST("master?flag=reg-step-I")
    Call<ResponseBody> signupDemo(
            @Part("name") RequestBody firstname,
            @Part("emailid") RequestBody emailid,
            @Part("phoneno") RequestBody phoneno,
            @Part("password") RequestBody password,
            @Part("term") RequestBody term,
            @Part("firebase_token") RequestBody token,
            @Part MultipartBody.Part userpic
    );


        @POST("update-token")
        @FormUrlEncoded
        Call<ResponseBody> updateToken(
                @Field("userid") int userId,
                @Field("firebase_token") String firebaseToken
        );

    @POST("api/update-token")
    @FormUrlEncoded
    Call<CommonPojoSuccess> updateTokenApi(
            @FieldMap HashMap<String,Object> hm);


    @POST("api/toggle-block-user")
    @FormUrlEncoded
    Call<CommonPojoSuccess> userBlockApi(
            @FieldMap HashMap<String,Object> hm);

    @POST("api/toggle-block-user")
    @FormUrlEncoded
    Call<CommonPojoSuccess> userBlockApi1(
            @FieldMap Map<String,Object> hm);
}
