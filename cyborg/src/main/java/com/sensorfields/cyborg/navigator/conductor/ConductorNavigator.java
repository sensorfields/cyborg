package com.sensorfields.cyborg.navigator.conductor;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.navigator.Screen;
import com.sensorfields.cyborg.navigator.Transaction;

import java.util.concurrent.Callable;

public final class ConductorNavigator implements Navigator {

    private Router router;

    @Override
    public void create(Activity activity, ViewGroup container, @Nullable Bundle savedInstanceState,
                       Callable<Screen> rootScreenFactory) {
        router = Conductor.attachRouter(activity, container, savedInstanceState);
        if (!router.hasRootController()) {
            try {
                root(rootScreenFactory.call());
            } catch (Exception e) {
                throw new IllegalArgumentException("Root screen creation failed", e);
            }
        }
    }

    @Override
    public boolean back() {
        return router.handleBack();
    }

    @Override
    public void execute(Transaction transaction) {
        if (transaction instanceof Transaction.RootTransaction) {
            router.setRoot(routerTransaction(((Transaction.RootTransaction) transaction).screen()));
        } else if (transaction instanceof Transaction.PushTransaction) {
            router.pushController(routerTransaction(
                    ((Transaction.PushTransaction) transaction).screen()));
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

    private static RouterTransaction routerTransaction(Screen screen) {
        return RouterTransaction.with((ConductorScreen) screen);
    }
}
