package com.app_neighbrsnook.apiService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bsy on 29/08/2017.
 */
public class ApiExecutor {
    public static String BASE_URL="";
    private static Retrofit retrofit;
    private static Retrofit retrofit2;
    public static ApiService getApiService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        httpClient.readTimeout(4, TimeUnit.HOURS);
        httpClient.writeTimeout(4, TimeUnit.HOURS);
        httpClient.connectTimeout(4, TimeUnit.MINUTES);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder().baseUrl(UrlClass.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
    public static ApiService getLaravelApiService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        httpClient.readTimeout(4, TimeUnit.HOURS);
        httpClient.writeTimeout(4, TimeUnit.HOURS);
        httpClient.connectTimeout(4, TimeUnit.MINUTES);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit2==null) {
            retrofit2 = new Retrofit.Builder().baseUrl(UrlClass.LARAVEL_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit2.create(ApiService.class);
    }
}