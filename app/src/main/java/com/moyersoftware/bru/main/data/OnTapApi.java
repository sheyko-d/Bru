package com.moyersoftware.bru.main.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Immutable model class for a On Tap Api item.
 */
public final class OnTapApi {

    @SerializedName("hours")
    private final String mHours;
    @SerializedName("on_tap_items")
    private final ArrayList<OnTap> mOnTapItems;

    public OnTapApi(String hours, ArrayList<OnTap> onTapItems) {
        mHours = hours;
        mOnTapItems = onTapItems;
    }

    public String getHours() {
        return mHours;
    }

    public ArrayList<OnTap> getOnTapItems() {
        return mOnTapItems;
    }
}
