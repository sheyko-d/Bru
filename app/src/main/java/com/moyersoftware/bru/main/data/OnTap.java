package com.moyersoftware.bru.main.data;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a On Tap item.
 */
public final class OnTap {

    public final static int TYPE_GROWLERS = 0;
    public final static int TYPE_CAN = 1;

    @SerializedName("id")
    private final String mId;
    @SerializedName("type")
    private final int mType;
    @SerializedName("name")
    private final String mName;
    @SerializedName("content")
    private final String mContent;
    @SerializedName("amount")
    private final String mAmount;
    @SerializedName("price")
    private final String mPrice;
    @SerializedName("text")
    private final String mText;

    public OnTap(String id, int type, String name, String content, String amount, String price,
                 String text) {
        mId = id;
        mType = type;
        mName = name;
        mContent = content;
        mAmount = amount;
        mPrice = price;
        mText = text;
    }

    public String getId() {
        return mId;
    }

    public int getType() {
        return mType;
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
