package com.sensorfields.cyborg.navigator.conductor;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.sensorfields.cyborg.mvi.MviViewModel;
import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.navigator.Screen;
import com.sensorfields.cyborg.navigator.Transaction;

import java.util.concurrent.Callable;

import timber.log.Timber;

public final class ConductorNavigator implements Navigator {

    private final Callable<Screen> rootScreenFactory;

    private Router router;

    public ConductorNavigator(Callable<Screen> rootScreenFactory) {
        this.rootScreenFactory = rootScreenFactory;
    }

    @Override
    public void onCreate(Activity activity, ViewGroup container,
                         @Nullable Bundle savedInstanceState) {
        router = Conductor.attachRouter(activity, container, savedInstanceState);
        if (!router.hasRootController()) {
            try {
                root(rootScreenFactory.call());
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
        Timber.e("onActivityResult: %s", requestCode);
    }

    @Override
    public void execute(Transaction transaction) {
        if (transaction instanceof Transaction.RootTransaction) {
            router.setRoot(routerTransaction(((Transaction.RootTransaction) transaction).screen()));
        } else if (transaction instanceof Transaction.PushTransaction) {
            router.pushController(routerTransaction(
                    ((Transaction.PushTransaction) transaction).screen()));
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
    public void root(Screen screen) {
        execute(Transaction.root(screen));
    }

    @Override
    public void push(Screen screen) {
        execute(Transaction.push(screen));
    }

    @Override
    public void activityForResult(int requestCode, Intent intent, @Nullable Bundle options) {
        execute(Transaction.activityForResult(requestCode, intent, options));
    }

    @Override
    public <T extends ViewModel & MviViewModel> T viewModel(Class<T> type) {
        return currentScreen().getViewModelProvider().get(type);
    }

    private Activity activity() {
        Activity activity = router.getActivity();
        if (activity == null) {
            throw new IllegalStateException("Activity is null");
        }
        return activity;
    }

    private ConductorScreen currentScreen() {
        return (ConductorScreen) router.getBackstack()
                .get(router.getBackstackSize() - 1)
                .controller();
    }

    private static RouterTransaction routerTransaction(Screen screen) {
        return RouterTransaction.with((ConductorScreen) screen);
    }
}
