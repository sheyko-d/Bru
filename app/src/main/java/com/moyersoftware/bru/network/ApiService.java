package com.moyersoftware.bru.network;

import com.moyersoftware.bru.user.model.Profile;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("user/sign_in_facebook.php")
    Call<Profile> signInFacebook(@Field("id") String id,
                                 @Field("name") String names,
                                 @Field("photo") String photo,
                                 @Field("email") String email);

    @FormUrlEncoded
    @POST("user/register.php")
    Call<Profile> register(@Field("name") String names,
                           @Field("email") String email,
                           @Field("password") String password);
}
