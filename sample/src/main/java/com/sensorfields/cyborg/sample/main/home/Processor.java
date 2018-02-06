package com.sensorfields.cyborg.sample.main.home;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

final class Processor implements ObservableTransformer<Action, Result> {

    @Inject Processor() {
    }

    @Override
    public ObservableSource<Result> apply(Observable<Action> upstream) {
        return upstream.map(action -> Result.RenderResult.create());
    }
}
