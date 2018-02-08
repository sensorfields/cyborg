package com.sensorfields.cyborg.sample.beer.detail;

import com.sensorfields.cyborg.mvi.BaseMviViewModel;

import javax.inject.Inject;

public final class BeerDetailViewModel extends BaseMviViewModel<Intent, ViewState, Action, Result> {

    @Inject BeerDetailViewModel(Processor processor) {
        super(processor);
    }

    @Override
    protected Class<? extends Intent> initialIntentType() {
        return Intent.InitialIntent.class;
    }

    @Override
    protected Action action(Intent intent) {
        if (intent instanceof Intent.InitialIntent) {
            return Action.ChooseAction.create();
        } else {
            throw new IllegalArgumentException("Unknown intent " + intent);
        }
    }

    @Override
    protected ViewState initialViewState() {
        return ViewState.initial();
    }
}
