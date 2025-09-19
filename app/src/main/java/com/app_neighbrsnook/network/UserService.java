package com.app_neighbrsnook.network;


import com.google.gson.JsonElement;
import com.app_neighbrsnook.model.LoginModel;
import com.app_neighbrsnook.model.pollDetailResponce.PollDetailPojo;
import com.app_neighbrsnook.model.pollResponce.PollPojo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface UserService {

    @FormUrlEncoded
    @POST("api/login")
    Call<LoginModel> login(@Field("email") String email,
                           @Field("password") String password);

    @FormUrlEncoded
    @POST("api/create-poll")
    Call<String> createPoll( @Header("Authorization") String BearerToken,
                                 @Field("poll_title") String poll_title,
                                 @Field("start_date") String start_date,
                                 @Field("end_date") String end_date,
                                 @Field("questions") String questions,
                                 @Field("options[0]") String options0,
                                 @Field("options[1]") String options1,
                                 @Field("options[2]") String options2,
                                 @Field("options[3]") String options3);

    @GET("api/polls-list")
    Call<PollPojo> getPollList(@Header("Authorization") String BearerToken);

    @FormUrlEncoded
    @POST("api/poll-details")
    Call<PollDetailPojo> getPollDetail(@Header("Authorization") String BearerToken,
                                       @Field("poll_id") int pollId);

    @GET("/entities/getRecentStories")
    Call<JsonElement> getStoriesData(@Query("city") String city);

    @GET("getLatestVideos")
    Call<JsonElement> getVideoes(
            @Query("city") String city,@Query("NCR&limit") String nl,@Query("start") String start,@Query("page") String page,
            @Query("userId=&token=&time") String token);

    @GET("/entities/getHomeBlocks")
    Call<JsonElement> getHomeBlock(
            @Query("city") String city
    );
}
