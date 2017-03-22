package com.moyersoftware.bru.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * Login
     */
    @FormUrlEncoded
    @POST("/user/login")
    Call<String> login(@Field("user_name") String userName,
                       @Field("password") String password,
                       @Field("device_token") String deviceToken,
                       @Query("time") long timestamp);
}
