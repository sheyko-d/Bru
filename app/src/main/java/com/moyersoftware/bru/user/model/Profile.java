package com.moyersoftware.bru.user.model;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a Profile.
 */
public final class Profile {

    @SerializedName("id")
    private final String mId;
    @SerializedName("name")
    private final String mName;
    @SerializedName("photo")
    private final String mPhoto;
    @SerializedName("email")
    private final String mEmail;
    @SerializedName("location")
    private final String mLocation;
    @SerializedName("token")
    private final String mToken;

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
}
