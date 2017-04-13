package com.moyersoftware.bru.main.data;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a Bru.
 */
public final class Bru {

    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("content")
    private String mContent;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("rating")
    private Float mRating;
    @SerializedName("my_rating")
    private Float mMyRating;
    @SerializedName("votes")
    private Integer mVotes;
    @SerializedName("color")
    private String mColor;

    public Bru(String id, String name, String content, String description, Float rating,
               Float myRating, Integer votes, String color) {
        mId = id;
        mName = name;
        mContent = content;
        mDescription = description;
        mRating = rating;
        mMyRating = myRating;
        mVotes = votes;
        mColor = color;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getContent() {
        return mContent;
    }

    public String getDescription() {
        return mDescription;
    }

    public Float getRating() {
        return mRating;
    }

    public Float getMyRating() {
        return mMyRating;
    }

    public Integer getVotes() {
        return mVotes;
    }

    public String getColor() {
        return mColor;
    }
}
