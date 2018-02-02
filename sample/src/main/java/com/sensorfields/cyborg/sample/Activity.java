package com.sensorfields.cyborg.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.sample.main.home.HomeScreen;

public class Activity extends AppCompatActivity {

    private Navigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = Application.component(this).navigator();
        navigator.create(this, findViewById(android.R.id.content), savedInstanceState,
                HomeScreen::create);
    }

    @Override
    public void onBackPressed() {
        if (!navigator.back()) {
            super.onBackPressed();
        }
    }
}
