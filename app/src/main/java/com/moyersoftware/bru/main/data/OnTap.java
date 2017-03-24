package com.moyersoftware.bru.main.data;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a On Tap item.
 */
public final class OnTap {

    public final static int TYPE_GROWLERS = 0;
    public final static int TYPE_CAN = 1;

    @SerializedName("id")
    private String mId;
    @SerializedName("type")
    private int mType;
    @SerializedName("name")
    private String mName;
    @SerializedName("content")
    private String mContent;
    @SerializedName("amount")
    private String mAmount;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("text")
    private String mText;
    @SerializedName("adapter_type")
    private int mAdapterType;

    public OnTap(String name, int adapterType) {
        mName = name;
        mAdapterType = adapterType;
    }

    public OnTap(String name, String text, int adapterType) {
        mName = name;
        mText = text;
        mAdapterType = adapterType;
    }

    public String getId() {
        return mId;
    }

    public int getType() {
        return mType;
    }

    public int getAdapterType() {
        return mAdapterType;
    }

    public String getName() {
        return mName;
    }

    public String getContent() {
        return mContent;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getText() {
        return mText;
    }
}
