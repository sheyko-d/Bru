package com.moyersoftware.bru.main.data;

/**
 * Immutable model class for a Friend.
 */
public final class NewsFeed {

    private final String mId;
    private final String mUserName;
    private final String mUserPhoto;
    private final String mLocation;
    private final String mText;
    private final String mImage;
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
