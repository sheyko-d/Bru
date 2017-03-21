package com.moyersoftware.bru.main.data;

/**
 * Immutable model class for a On Tap item.
 */
public final class OnTap {

    public final static int TYPE_CAN = 0;
    public final static int TYPE_GROWLERS = 1;

    private final String mId;
    private final int mType;
    private final String mName;
    private final String mContent;
    private final String mAmount;
    private final String mPrice;
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
