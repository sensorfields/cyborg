package com.sensorfields.cyborg.sample.beer.list;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

final class Processor implements ObservableTransformer<Action, Result> {

    private final ObservableTransformer<Action.RenderAction, Result.RenderResult> render;

    @Inject Processor() {
        render = upstream -> upstream.map(action -> Result.RenderResult.create());
    }

    @SuppressWarnings("unchecked")
    @Override
    public ObservableSource<Result> apply(Observable<Action> upstream) {
        return upstream.publish(shared -> Observable.mergeArray(
                shared.ofType(Action.RenderAction.class).compose(render)));
    }
}
