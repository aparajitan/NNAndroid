package com.app_neighbrsnook.apiService;

import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.model.postComment.CommentLikePojo;
import com.app_neighbrsnook.pojo.GroupJoinPojo;
import com.app_neighbrsnook.pojo.NeighbhoodAddressModel;
import com.app_neighbrsnook.pojo.RegisterationOtpPojo;
import com.app_neighbrsnook.pojo.StateDropdownHomePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.DeleteMarketPlaceProduct;
import com.app_neighbrsnook.pojo.marketPlacePojo.ParentChatWithSenderRcvr;
import com.app_neighbrsnook.pojo.marketPlacePojo.UsersListChatsPojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {
    @FormUrlEncoded
    @POST(UrlClass.REQUEST_FOR_NEIGHBRHOOD)
    Call<AddressResponse> requestNeighbrhood(
            @Query("flag") String falg,
            @FieldMap HashMap<String, String> hashMap
    );


    @FormUrlEncoded
    @POST(UrlClass.LOCATION_STATUS)
    Call<AddressResponse> locationAuto(
            @Query("flag") String falg,
            @FieldMap HashMap<String, String> hashMap
    );

    @FormUrlEncoded
    @POST(UrlClass.USERNAME_CHANGED)
    Call<AddressResponse> usernameUpdate(
            @Query("flag") String falg,
            @FieldMap HashMap<String, String> hashMap
    );
    @FormUrlEncoded
    @POST(UrlClass.CHANGE_PASSWORD)
    Call<AddressResponse> updateApiPassword(
            @Query("flag") String falg,
            @FieldMap HashMap<String, String> hashMap
    );
    @FormUrlEncoded
    @POST(UrlClass.DEACTIVATE_ACCOUNT)
    Call<AddressResponse> deactivateAccount(
            @Query("flag") String falg,
            @FieldMap HashMap<String, Object> hashMap
    );
    @FormUrlEncoded
    @POST(UrlClass.SAVE_NEIGHBRHOOD)
    Call<AddressResponse> neighbrhoodSaved(
            @Query("flag") String falg,
            @FieldMap HashMap<String, String> hashMap
    );

    @FormUrlEncoded
    @POST(UrlClass.PROFILE)
    Call<AddressResponse> profileMethod(
            @Query("flag") String falg,
            @FieldMap HashMap<String, String> hashMap
    );
    @FormUrlEncoded
    @POST(UrlClass.NEIGHBRHOOD_LIST)
    Call<AddressResponse> neighbrhoodSelection(
            @Query("flag") String falg,
            @FieldMap HashMap<String, String> hashMap
    );
    @FormUrlEncoded
    @POST("api/master")
    Call<AddressResponse> skipScreenStep(
            @Query("flag") String falg,
            @FieldMap HashMap<String, String> hashMap
    );

  @POST()
  Call<StateDropdownHomePojo> getDropdownData(
          @Url String url);
  @GET()
  Call<UsersListChatsPojo> getChatListData(
          @Url String url);

  @DELETE()
  Call<DeleteMarketPlaceProduct> getDeleteProduct(
          @Url String url);


  @DELETE()
  Call<GroupJoinPojo> getDeleteWishlist(
          @Url String url);



  @GET()
  Call<ParentChatWithSenderRcvr> getChatSederRcvr(
          @Url String url);

  @Multipart
  @POST(UrlClass.LITTEL_MORE_ABOUT)
  Call<AddressResponse> moreaboutYou(
          @Query("flag") String falg,
          @Part MultipartBody.Part userpic,
          @PartMap() Map<String, RequestBody> partMap
  );
  @Multipart
  @POST(UrlClass.PROFILE_UPDATE)
  Call<AddressResponse> profileImageUpdate(
          @Query("flag") String falg,
          @Part MultipartBody.Part aadharFront,
          @PartMap() Map<String, RequestBody> partMap
  );
  @Multipart
  @POST(UrlClass.EVENT_IMAGE_SEND)
  Call<AddressResponse> eventImage(
          @Query("flag") String falg,
          @Part MultipartBody.Part aadharFront,
          @PartMap() Map<String, RequestBody> partMap
  );

  @Multipart
  @POST(UrlClass.ADDRESS_PROOF_API)
  Call<AddressResponse> addressProofPhoto(
          @Query("flag") String falg,
          @Part MultipartBody.Part aadharFront,
          @Part MultipartBody.Part aadharBack,
          @Part MultipartBody.Part passportFront,
          @Part MultipartBody.Part passportBack,
          @Part MultipartBody.Part voterFront,
          @Part MultipartBody.Part voterBack,
          @Part MultipartBody.Part dlFront,
          @Part MultipartBody.Part dlBack,
          @PartMap() Map<String, RequestBody> partMap
  );
  @Multipart
  @POST(UrlClass.ADDRESS_PROOF_API)
  Call<AddressResponse> addressProofPhotoLast(
          @Query("flag") String falg,
          @Part MultipartBody.Part imgFront,
          @Part MultipartBody.Part imgBack,
          @PartMap() Map<String, RequestBody> partMap
  );
 @Multipart
  @POST(UrlClass.ADDRESS_PROOF_API)
  Call<NeighbhoodAddressModel> addressProofPhoto(
          @Query("flag") String falg,
          @Part MultipartBody.Part imgFront,
          @Part MultipartBody.Part imgBack,
          @PartMap() Map<String, RequestBody> partMap
  );


    @FormUrlEncoded
    @POST(UrlClass.ADDRESS_PROOF_API)
    Call<AddressResponse> reachoutIIndStep(
            @Query("flag") String flag,
            @FieldMap Map<String, String> fields
    );

    @Multipart
  @POST(UrlClass.CREATE_GROUP_URL)
  Call<AddressResponse> createGroup(
          @Query("flag") String falg,
          @Part MultipartBody.Part aadharFront,
          @PartMap() Map<String, RequestBody> partMap
  );

    @Multipart
    @POST(UrlClass.CREATE_EVENT_URL)
    Call<AddressResponse> eventcreat(
            @Query("flag") String falg,
            @Part MultipartBody.Part createImage,
            @PartMap() Map<String, RequestBody> partMap
    );

    @Multipart
    @POST(UrlClass.CREATE_EVENT_URL)
    Call<AddressResponse> eventImageUpload(
            @Query("flag") String falg,
            @PartMap() Map<String, RequestBody> partMap
    );
    @Multipart
    @POST(UrlClass.CREATE_PRODUCT_URL)
    Call<AddressResponse> createProduct(
            @Part List<MultipartBody.Part> userpic,
            @PartMap Map<String, RequestBody> partMap,
            @Header("Accept") String acceptHeader
    );




    @FormUrlEncoded
    @GET(UrlClass.HOMEPAGE_LIST_API)
    Call<AddressResponse> homePageList();

  @Multipart
  @POST(UrlClass.CREATE_GROUP_URL)
  Call<AddressResponse> updateGroup(
          @Query("flag") String falg,
          @Part MultipartBody.Part aadharFront,
          @PartMap() Map<String, RequestBody> partMap
  );

  @FormUrlEncoded
  @POST
    Call<Object> postRequest(
            @Url String url,
            @FieldMap Map<String, String> hashMap
  );


}
