package com.sensorfields.cyborg.sample.main.home;

import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.sample.beer.list.BeerListScreen;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

final class Processor implements ObservableTransformer<Action, Result> {

    private final ObservableTransformer<Action.RenderAction, Result.RenderResult> render;

    private final ObservableTransformer<Action.BeerListAction, Result.RenderResult> beerList;

    @Inject Processor(Navigator navigator) {
        render = upstream -> upstream.map(action -> Result.RenderResult.create());

        beerList = upstream -> upstream
                .filter(action -> {
                    navigator.push(BeerListScreen.create());
                    return false;
                })
                .map(action -> Result.RenderResult.create());
    }

    @SuppressWarnings("unchecked")
    @Override
    public ObservableSource<Result> apply(Observable<Action> upstream) {
        return upstream.publish(shared -> Observable.mergeArray(
                shared.ofType(Action.RenderAction.class).compose(render),
                shared.ofType(Action.BeerListAction.class).compose(beerList)));
    }
}
