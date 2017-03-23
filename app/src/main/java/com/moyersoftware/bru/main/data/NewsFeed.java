package com.moyersoftware.bru.main.data;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a News Feed item.
 */
public final class NewsFeed {

    @SerializedName("id")
    private final String mId;
    @SerializedName("user_name")
    private final String mUserName;
    @SerializedName("user_photo")
    private final String mUserPhoto;
    @SerializedName("location")
    private final String mLocation;
    @SerializedName("text")
    private final String mText;
    @SerializedName("image")
    private final String mImage;
    @SerializedName("time")
    private final String mTime;

    public NewsFeed(String id, String userName, String userPhoto, String location, String text,
                    String image, String time) {
        mId = id;
        mUserName = userName;
        mUserPhoto = userPhoto;
        mLocation = location;
        mText = text;
        mImage = image;
        mTime = time;
    }

    public String getId() {
        return mId;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getUserPhoto() {
        return mUserPhoto;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getText() {
        return mText;
    }

    public String getImage() {
        return mImage;
    }

    public String getTime() {
        return mTime;
    }
}
