package com.moyersoftware.bru.util;

import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.moyersoftware.bru.app.BruApp;
import com.moyersoftware.bru.user.model.Profile;

public class Util {

    private static final String LOG_TAG = "BruDebug";
    public static final String BASE_API_URL = "http://moyersoftware.com/bru/";
    private static final int DEBUG_MAX_LENGTH = 500;
    private static final String PREF_PROFILE = "Profile";
    private static final String PREF_NOTIFICATIONS = "Notifications";
    private static final String PREF_TUTORIAL_SHOWN = "TutorialShown";

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
        return getProfile() != null;
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

    /**
     * Saves the profile of the logged in user in preferences.
     */
    public static void setProfile(Profile profile) {
        PreferenceManager.getDefaultSharedPreferences(BruApp.getContext()).edit()
                .putString(PREF_PROFILE, new Gson().toJson(profile)).apply();
    }

    /**
     * Retrieves the profile of the logged in user from preferences.
     */
    public static Profile getProfile() {
        return new Gson().fromJson(PreferenceManager.getDefaultSharedPreferences
                (BruApp.getContext()).getString(PREF_PROFILE, null), Profile.class);
    }

    public static boolean notificationsEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(BruApp.getContext())
                .getBoolean(PREF_NOTIFICATIONS, true);
    }

    public static void setNotificationsEnabled(boolean enabled) {
        PreferenceManager.getDefaultSharedPreferences(BruApp.getContext()).edit()
                .putBoolean(PREF_NOTIFICATIONS, enabled).apply();
    }

    public static void setTutorialShown() {
        PreferenceManager.getDefaultSharedPreferences(BruApp.getContext()).edit()
                .putBoolean(PREF_TUTORIAL_SHOWN, true)
                .apply();
    }

    public static boolean isTutorialShown() {
        return PreferenceManager.getDefaultSharedPreferences(BruApp.getContext())
                .getBoolean(PREF_TUTORIAL_SHOWN, false);
    }
}
