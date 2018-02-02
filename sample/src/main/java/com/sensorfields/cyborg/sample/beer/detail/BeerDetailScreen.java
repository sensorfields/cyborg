package com.sensorfields.cyborg.sample.beer.detail;

import android.content.Context;
import android.view.View;

import com.sensorfields.cyborg.navigator.conductor.ConductorScreen;
import com.sensorfields.cyborg.sample.R;

public final class BeerDetailScreen extends ConductorScreen {

    public static BeerDetailScreen create() {
        return new BeerDetailScreen();
    }

    @SuppressWarnings("WeakerAccess")
    public BeerDetailScreen() {
        super(R.id.beerDetailScreen);
    }

    @Override
    protected View view(Context context) {
        return new BeerDetailView(context);
    }
}
