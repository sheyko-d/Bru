package com.moyersoftware.bru.util;

import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.moyersoftware.bru.app.BruApp;

public class Util {

    private static final String LOG_TAG = "BruDebug";
    public static final String BASE_API_URL = "";
    public static final int DEBUG_MAX_LENGTH = 500;

    /**
     * Adds a message to LogCat.
     */
    public static void Log(Object text) {
        Log.d(LOG_TAG, text + "");
    }

    /**
     * Converts from DP (density-independent pixels) to regular pixels.
     */
    public static int convertDpToPixel(float dp) {
        Resources resources = BruApp.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    /**
     * Checks if user is logged in.
     */
    public static boolean isLoggedIn() {
        return false;
    }

    /**
     * Checks if Android version of the device is more than Android 5.0 Lollipop.
     */
    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Split output log by lines
     *
     * @param tag  tag
     * @param data data to output
     */
    @SuppressWarnings("unused")
    public static void splitOutput(String tag, String data) {
        if (data == null)
            return;
        int i = 0;
        while (i < data.length()) {
            Log.d(tag, data.substring(i, i + DEBUG_MAX_LENGTH > data.length() ? data.length() : i
                    + DEBUG_MAX_LENGTH));
            i += DEBUG_MAX_LENGTH;
        }
    }
}
