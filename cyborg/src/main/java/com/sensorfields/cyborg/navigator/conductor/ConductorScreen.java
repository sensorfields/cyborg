package com.sensorfields.cyborg.navigator.conductor;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.sensorfields.cyborg.navigator.Screen;

public abstract class ConductorScreen extends Controller implements Screen, ViewModelStoreOwner {

    private final int id;

    private final ViewModelStore viewModelStore = new ViewModelStore();
    private final ViewModelProvider viewModelProvider =
            new ViewModelProvider(this, new ViewModelProvider.Factory() {
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (viewModelFactory == null) {
                throw new IllegalStateException("Context is unavailable to create a view model");
            }
            return viewModelFactory.create(modelClass);
        }
    });
    @Nullable private ViewModelProvider.Factory viewModelFactory;

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

    ViewModelProvider getViewModelProvider() {
        return viewModelProvider;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    @Override
    protected void onContextAvailable(@NonNull Context context) {
        super.onContextAvailable(context);
        viewModelFactory = (ViewModelProvider.Factory) context.getApplicationContext();
    }

    @Override
    protected void onContextUnavailable() {
        viewModelFactory = null;
        super.onContextUnavailable();
    }
}
