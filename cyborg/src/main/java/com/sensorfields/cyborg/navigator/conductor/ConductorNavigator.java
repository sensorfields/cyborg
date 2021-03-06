package com.sensorfields.cyborg.navigator.conductor;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStore;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.sensorfields.cyborg.navigator.ActivityResult;
import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.navigator.Screen;
import com.sensorfields.cyborg.navigator.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

public final class ConductorNavigator implements Navigator {

    private final Callable<Screen> rootScreenFactory;
    private final ViewModelProvider.Factory viewModelFactory;
    private final Map<String, ScreenViewModelProvider> viewModelProviders;
    private final Subject<ActivityResult> activityResultSubject;

    private Router router;

    public ConductorNavigator(Callable<Screen> rootScreenFactory,
                              ViewModelProvider.Factory viewModelFactory) {
        this.rootScreenFactory = rootScreenFactory;
        this.viewModelFactory = viewModelFactory;
        this.viewModelProviders = new HashMap<>();
        this.activityResultSubject = PublishSubject.create();
    }

    @Override
    public void onCreate(Activity activity, ViewGroup container,
                         @Nullable Bundle savedInstanceState) {
        router = Conductor.attachRouter(activity, container, savedInstanceState);
        router.addChangeListener(new ControllerChangeHandler.ControllerChangeListener() {
            @Override
            public void onChangeStarted(@Nullable Controller to, @Nullable Controller from,
                                        boolean isPush, @NonNull ViewGroup container,
                                        @NonNull ControllerChangeHandler handler) {
            }
            @Override
            public void onChangeCompleted(@Nullable Controller to, @Nullable Controller from,
                                          boolean isPush, @NonNull ViewGroup container,
                                          @NonNull ControllerChangeHandler handler) {
                Timber.d("onChangeCompleted:\nFROM:  %s\nTO:    %s\nPUSH:  %s\nSTACK: %s", from, to,
                        isPush, stack(router));
                if (!isPush && from != null) {
                    String screenId = from.getInstanceId();
                    if (viewModelProviders.containsKey(screenId)) {
                        viewModelProviders.get(screenId).clear();
                        viewModelProviders.remove(screenId);
                    }
                }
            }
        });
        if (!router.hasRootController()) {
            try {
                execute(Transaction.root(rootScreenFactory.call()));
            } catch (Exception e) {
                throw new IllegalStateException("Root screen creation failed", e);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        return router.handleBack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Timber.d("onActivityResult: %s, %s, %s", requestCode, resultCode, data);
        activityResultSubject.onNext(ActivityResult.create(requestCode, resultCode, data));
    }

    @Override
    public <T extends ViewModel> T viewModel(Class<T> type) {
        String screenId = router.getBackstack()
                .get(router.getBackstackSize() - 1)
                .controller()
                .getInstanceId();
        if (!viewModelProviders.containsKey(screenId)) {
            viewModelProviders.put(screenId, ScreenViewModelProvider.create(viewModelFactory));
        }
        return viewModelProviders.get(screenId).get(type);
    }

    @Override
    public void execute(Transaction transaction) {
        Timber.d("Execute: %s", transaction);
        if (transaction instanceof Transaction.RootTransaction) {
            router.setRoot(routerTransaction(((Transaction.RootTransaction) transaction).screen()));
        } else if (transaction instanceof Transaction.PushTransaction) {
            router.pushController(routerTransaction(
                    ((Transaction.PushTransaction) transaction).screen()));
        } else if (transaction instanceof Transaction.PopTransaction) {
            router.popCurrentController();
        } else if (transaction instanceof Transaction.ActivityForResultTransaction) {
            Transaction.ActivityForResultTransaction activityForResultTransaction =
                    (Transaction.ActivityForResultTransaction) transaction;
            activity().startActivityForResult(activityForResultTransaction.intent(),
                    activityForResultTransaction.requestCode(),
                    activityForResultTransaction.options());
        } else {
            throw new IllegalArgumentException("Unknown transaction " + transaction);
        }
    }

    @Override
    public Observable<ActivityResult> activityResults() {
        return activityResultSubject;
    }

    private Activity activity() {
        Activity activity = router.getActivity();
        if (activity == null) {
            throw new IllegalStateException("Activity is null");
        }
        return activity;
    }

    private static RouterTransaction routerTransaction(Screen screen) {
        return RouterTransaction.with((ConductorScreen) screen);
    }

    static final class ScreenViewModelProvider extends ViewModelProvider {

        static ScreenViewModelProvider create(Factory factory) {
            return new ScreenViewModelProvider(new ViewModelStore(), factory);
        }

        private final ViewModelStore store;

        ScreenViewModelProvider(@NonNull ViewModelStore store, @NonNull Factory factory) {
            super(store, factory);
            this.store = store;
        }

        void clear() {
            store.clear();
        }
    }

    private static List<Controller> stack(Router router) {
        List<Controller> controllers = new ArrayList<>();
        for (RouterTransaction transaction : router.getBackstack()) {
            controllers.add(transaction.controller());
        }
        return controllers;
    }
}
