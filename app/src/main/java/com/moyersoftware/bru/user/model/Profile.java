package com.moyersoftware.bru.user.model;

/**
 * Immutable model class for a Profile.
 */
public final class Profile {

    private final String mId;
    private final String mName;
    private final String mPhoto;
    private final String mEmail;

    public Profile(String id, String name, String photo, String email) {
        mId = id;
        mName = name;
        mPhoto = photo;
        mEmail = email;
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
}
