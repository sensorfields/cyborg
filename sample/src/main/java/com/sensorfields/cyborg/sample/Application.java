package com.sensorfields.cyborg.sample;

import android.os.StrictMode;

import timber.log.Timber;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        setupStrictMode();
        super.onCreate();
        setupTimber();
    }

    // StrictMode

    private void setupStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }
    }

    // Timber

    protected void setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
