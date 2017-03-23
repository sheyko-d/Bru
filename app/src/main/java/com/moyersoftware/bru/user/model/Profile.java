package com.moyersoftware.bru.user.model;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a Profile.
 */
public final class Profile {

    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("photo")
    private String mPhoto;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("token")
    private String mToken;

    public Profile(String id, String name, String photo, String email, String location,
                   String token) {
        mId = id;
        mName = name;
        mPhoto = photo;
        mEmail = email;
        mLocation = location;
        mToken = token;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getToken() {
        return mToken;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public void setPhoto(String photo) {
        this.mPhoto = photo;
    }
}
