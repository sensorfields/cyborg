package com.sensorfields.cyborg.navigator;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.sensorfields.cyborg.mvi.MviViewModel;

import java.util.concurrent.Callable;

public interface Navigator {

    void create(Activity activity, ViewGroup container, @Nullable Bundle savedInstanceState,
                Callable<Screen> rootScreenFactory);

    boolean back();

    void execute(Transaction transaction);

    void root(Screen screen);

    void push(Screen screen);

    void activityForResult(int requestCode, Intent intent, @Nullable Bundle options);

    <T extends ViewModel & MviViewModel> T viewModel(Class<T> type);
}
