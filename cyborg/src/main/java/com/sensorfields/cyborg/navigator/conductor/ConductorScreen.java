package com.sensorfields.cyborg.navigator.conductor;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.sensorfields.cyborg.navigator.Screen;

public abstract class ConductorScreen extends Controller implements Screen {

    private final int id;

    protected ConductorScreen(@IdRes int id) {
        this.id = id;
    }

    protected abstract View view(Context context);

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = view(container.getContext());
        view.setId(id);
        return view;
    }
}
