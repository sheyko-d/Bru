package com.moyersoftware.bru.network;

import com.moyersoftware.bru.main.data.NewsFeed;
import com.moyersoftware.bru.main.data.OnTapApi;
import com.moyersoftware.bru.user.model.Profile;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @FormUrlEncoded
    @POST("user/sign_in_facebook.php")
    Call<Profile> signInFacebook(@Field("id") String id,
                                 @Field("name") String names,
                                 @Field("photo") String photo,
                                 @Field("email") String email);

    @FormUrlEncoded
    @POST("user/register.php")
    Call<Profile> register(@Field("name") String name,
                           @Field("email") String email,
                           @Field("password") String password);

    @FormUrlEncoded
    @POST("user/login.php")
    Call<Profile> login(@Field("email") String email,
                        @Field("password") String password);

    @FormUrlEncoded
    @POST("user/update_location.php")
    Call<Void> updateLocation(@Field("latitude") double latitude,
                              @Field("longitude") double longitude,
                              @Field("token") String token);

    @GET("news_feed/get_news_feed.php")
    Call<ArrayList<NewsFeed>> getNewsFeed();

    @FormUrlEncoded
    @POST("news_feed/delete_news_feed_item.php")
    Call<Void> deleteNewsFeedItem(@Field("news_feed_id") String newsFeedId,
                                  @Field("token") String token);

    @GET("on_tap/get_on_tap.php")
    Call<OnTapApi> getOnTap();

    @Multipart
    @POST("news_feed/add_post.php")
    Call<Void> addPost(@Part("text") String text,
                       @Part("token") String token,
                       @Part MultipartBody.Part file);

    @Multipart
    @POST("user/update_profile.php")
    Call<Void> updateProfile(@Part("name") String name,
                             @Part("email") String email,
                             @Part("token") String token,
                             @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("user/update_google_token.php")
    Call<Void> updateGoogleToken(@Field("google_token") String googleToken,
                                 @Field("token") String token);
}
