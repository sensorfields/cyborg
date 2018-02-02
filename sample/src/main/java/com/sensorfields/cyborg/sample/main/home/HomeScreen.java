package com.sensorfields.cyborg.sample.main.home;

import android.content.Context;
import android.view.View;

import com.sensorfields.cyborg.navigator.conductor.ConductorScreen;
import com.sensorfields.cyborg.sample.R;

public final class HomeScreen extends ConductorScreen {

    public static HomeScreen create() {
        return new HomeScreen();
    }

    @SuppressWarnings("WeakerAccess")
    public HomeScreen() {
        super(R.id.mainHomeScreen);
    }

    @Override
    protected View view(Context context) {
        return new HomeView(context);
    }
}
