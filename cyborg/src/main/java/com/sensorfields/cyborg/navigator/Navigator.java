package com.sensorfields.cyborg.navigator;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.sensorfields.cyborg.mvi.MviViewModel;

public interface Navigator {

    /**
     * Has to be called in {@link Activity#onCreate(Bundle)}.
     *
     * @param activity Activity instance.
     * @param container Container for screens.
     * @param savedInstanceState Saved instance state.
     */
    void onCreate(Activity activity, ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * Has to be called in {@link Activity#onBackPressed()}.
     *
     * if (!navigator.onBackPressed()) {
     *     super.onBackPressed();
     * }
     *
     * @return Whether there were any screens in the back stack.
     */
    boolean onBackPressed();

    /**
     * Has to be called in {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode Request code.
     * @param resultCode Result code.
     * @param data Data.
     */
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    void execute(Transaction transaction);

    void root(Screen screen);

    void push(Screen screen);

    void activityForResult(int requestCode, Intent intent, @Nullable Bundle options);

    <T extends ViewModel & MviViewModel> T viewModel(Class<T> type);
}
