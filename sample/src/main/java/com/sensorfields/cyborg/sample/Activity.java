package com.sensorfields.cyborg.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.navigator.Screen;
import com.sensorfields.cyborg.sample.main.home.HomeScreen;

import java.util.concurrent.Callable;

import javax.inject.Inject;

public class Activity extends AppCompatActivity {

    private Navigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = Application.component(this).navigator();
        navigator.onCreate(this, findViewById(android.R.id.content), savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (!navigator.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        navigator.onActivityResult(requestCode, resultCode, data);
    }

    static final class RootScreenFactory implements Callable<Screen> {

        @Inject RootScreenFactory() {}

        @Override
        public Screen call() throws Exception {
            return HomeScreen.create();
        }
    }
}
