package com.moyersoftware.bru.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.crashlytics.android.answers.Answers;
import com.moyersoftware.bru.R;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BruApp extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Answers());
        sContext = this;

        // Init calligraphy library
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static Context getContext() {
        return sContext;
    }
}
