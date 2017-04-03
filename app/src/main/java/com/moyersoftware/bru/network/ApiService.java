package com.moyersoftware.bru.network;

import com.moyersoftware.bru.main.data.Bru;
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
    @POST("api/v1/user/sign_in_facebook.php")
    Call<Profile> signInFacebook(@Field("id") String id,
                                 @Field("name") String names,
                                 @Field("photo") String photo,
                                 @Field("email") String email);

    @FormUrlEncoded
    @POST("api/v1/user/register.php")
    Call<Profile> register(@Field("name") String name,
                           @Field("email") String email,
                           @Field("password") String password);

    @FormUrlEncoded
    @POST("api/v1/user/login.php")
    Call<Profile> login(@Field("email") String email,
                        @Field("password") String password);

    @FormUrlEncoded
    @POST("api/v1/user/update_location.php")
    Call<Void> updateLocation(@Field("latitude") double latitude,
                              @Field("longitude") double longitude,
                              @Field("token") String token);

    @GET("api/v1/news_feed/get_news_feed.php")
    Call<ArrayList<NewsFeed>> getNewsFeed();

    @GET("api/v1/on_tap/get_on_tap.php")
    Call<OnTapApi> getOnTap();

    @FormUrlEncoded
    @POST("api/v1/bru/get_brus.php")
    Call<ArrayList<Bru>> getBrus(@Field("token") String token);

    @FormUrlEncoded
    @POST("api/v1/bru/rate_bru.php")
    Call<Void> rateBru(@Field("token") String token, @Field("bru_id") String bruId,
                       @Field("rating") Float rating);

    @FormUrlEncoded
    @POST("api/v1/news_feed/delete_news_feed_item.php")
    Call<Void> deleteNewsFeedItem(@Field("news_feed_id") String newsFeedId,
                                  @Field("token") String token);

    @Multipart
    @POST("api/v1/news_feed/add_post.php")
    Call<Void> addPost(@Part("text") String text,
                       @Part("token") String token,
                       @Part MultipartBody.Part file);

    @Multipart
    @POST("api/v1/user/update_profile.php")
    Call<Void> updateProfile(@Part("name") String name,
                             @Part("email") String email,
                             @Part("token") String token,
                             @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/v1/user/update_google_token.php")
    Call<Void> updateGoogleToken(@Field("google_token") String googleToken,
                                 @Field("token") String token);

    @GET("scraper/scrape_treehouse.php")
    Call<Void> updateBeers();
}
