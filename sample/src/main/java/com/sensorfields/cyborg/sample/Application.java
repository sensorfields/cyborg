package com.sensorfields.cyborg.sample;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.navigator.conductor.ConductorNavigator;
import com.sensorfields.cyborg.sample.beer.list.BeerListViewModel;
import com.sensorfields.cyborg.sample.main.home.HomeViewModel;

import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
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
        static Navigator navigator(Activity.RootScreenFactory rootScreenFactory,
                                   ViewModelProvider.Factory viewModelFactory) {
            return new ConductorNavigator(rootScreenFactory, viewModelFactory);
        }

        @Provides @Singleton
        static ViewModelProvider.Factory viewModelFactory(
                Map<Class<?>, Provider<ViewModel>> viewModelProviders) {
            return new ViewModelFactory(viewModelProviders);
        }

        @SuppressWarnings("unused")
        @Binds @IntoMap @ClassKey(HomeViewModel.class)
        abstract ViewModel homeViewModel(HomeViewModel homeViewModel);

        @SuppressWarnings("unused")
        @Binds @IntoMap @ClassKey(BeerListViewModel.class)
        abstract ViewModel beerListViewModel(BeerListViewModel beerListViewModel);
    }

    static final class ViewModelFactory implements ViewModelProvider.Factory {

        private final Map<Class<?>, Provider<ViewModel>> viewModelProviders;

        ViewModelFactory(Map<Class<?>, Provider<ViewModel>> viewModelProviders) {
            this.viewModelProviders = viewModelProviders;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) viewModelProviders.get(modelClass).get();
        }
    }
}
