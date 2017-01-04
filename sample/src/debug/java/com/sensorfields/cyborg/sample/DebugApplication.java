package com.sensorfields.cyborg.sample;

import android.os.StrictMode;

import timber.log.Timber;

public class DebugApplication extends Application {

    @Override
    public void onCreate() {
        StrictMode.enableDefaults();
        super.onCreate();
    }

    // Timber

    @Override
    protected void setupTimber() {
        Timber.plant(new Timber.DebugTree());
    }
}
