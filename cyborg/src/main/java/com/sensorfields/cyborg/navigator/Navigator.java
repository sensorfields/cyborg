package com.sensorfields.cyborg.navigator;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import java.util.concurrent.Callable;

public interface Navigator {

    void create(Activity activity, ViewGroup container, @Nullable Bundle savedInstanceState,
                Callable<Screen> rootScreenFactory);

    boolean back();

    void execute(Transaction transaction);

    void root(Screen screen);

    void push(Screen screen);
}
