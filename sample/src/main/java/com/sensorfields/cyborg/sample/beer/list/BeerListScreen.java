package com.sensorfields.cyborg.sample.beer.list;

import android.content.Context;
import android.view.View;

import com.sensorfields.cyborg.navigator.conductor.ConductorScreen;
import com.sensorfields.cyborg.sample.R;

public final class BeerListScreen extends ConductorScreen {

    public static BeerListScreen create() {
        return new BeerListScreen();
    }

    @SuppressWarnings("WeakerAccess")
    public BeerListScreen() {
        super(R.id.beerListScreen);
    }

    @Override
    protected View view(Context context) {
        return new BeerListView(context);
    }
}
