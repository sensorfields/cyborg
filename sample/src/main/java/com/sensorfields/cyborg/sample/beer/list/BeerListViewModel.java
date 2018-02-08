package com.sensorfields.cyborg.sample.beer.list;

import com.sensorfields.cyborg.mvi.BaseMviViewModel;

import javax.inject.Inject;

import timber.log.Timber;

public final class BeerListViewModel extends BaseMviViewModel<Intent, ViewState, Action, Result> {

    @Inject BeerListViewModel(Processor processor) {
        super(processor);
        Timber.e("Constructor");
    }

    @Override
    protected Class<? extends Intent> initialIntentType() {
        return Intent.InitialIntent.class;
    }

    @Override
    protected Action action(Intent intent) {
        if (intent instanceof Intent.InitialIntent) {
            return Action.RenderAction.create();
        } else if (intent instanceof Intent.ChooseIntent) {
            return Action.ChooseAction.create();
        } else if (intent instanceof Intent.DetailIntent) {
            return Action.DetailAction.create();
        } else {
            throw new IllegalArgumentException("Unknown intent " + intent);
        }
    }

    @Override
    protected ViewState initialViewState() {
        return ViewState.initial();
    }
}
