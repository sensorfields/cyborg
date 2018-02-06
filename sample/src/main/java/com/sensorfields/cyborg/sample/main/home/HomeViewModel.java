package com.sensorfields.cyborg.sample.main.home;

import com.sensorfields.cyborg.mvi.BaseMviViewModel;

import javax.inject.Inject;

import timber.log.Timber;

public final class HomeViewModel extends BaseMviViewModel<Intent, ViewState, Action, Result> {

    @Inject HomeViewModel(Processor processor) {
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
        } else if (intent instanceof Intent.BeerListIntent) {
            return Action.BeerListAction.create();
        } else {
            throw new IllegalArgumentException("Unknown intent " + intent);
        }
    }

    @Override
    protected ViewState initialViewState() {
        return ViewState.initial();
    }
}
