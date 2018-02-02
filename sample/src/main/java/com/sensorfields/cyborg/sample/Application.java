package com.sensorfields.cyborg.sample;

import android.content.Context;
import android.os.StrictMode;

import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.navigator.conductor.ConductorNavigator;

import javax.inject.Singleton;

import dagger.Provides;
import timber.log.Timber;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        setupStrictMode();
        super.onCreate();
        setupTimber();
        setupDagger();
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

    // Dagger

    private Component component;

    private void setupDagger() {
        component = DaggerApplication_Component.create();
    }

    public static Component component(Context context) {
        return ((Application) context.getApplicationContext()).component;
    }

    @Singleton
    @dagger.Component(modules = Module.class)
    public interface Component {

        Navigator navigator();
    }

    @dagger.Module
    static abstract class Module {

        @Provides @Singleton
        static Navigator navigator() {
            return new ConductorNavigator();
        }
    }
}
