package com.sensorfields.cyborg.sample.beer.detail;

import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.navigator.Transaction;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

final class Processor implements ObservableTransformer<Action, Result> {

    private final ObservableTransformer<Action.ChooseAction, Result.RenderResult> choose;

    @Inject Processor(Navigator navigator) {
        choose = upstream -> upstream
                .switchMap(action -> {
                    navigator.execute(action.transaction());
                    return navigator.activityResults()
                            .filter(activityResult ->
                                    activityResult.requestCode()
                                            == action.transaction().requestCode())
                            .doOnNext(activityResult -> {
                                throw new IllegalArgumentException("asd");
                            })
                            .map(activityResult -> Result.RenderResult.create());
                })
                .onErrorReturn(throwable -> {
                    navigator.execute(Transaction.pop());
                    return Result.RenderResult.create();
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public ObservableSource<Result> apply(Observable<Action> upstream) {
        return upstream.publish(shared -> Observable.mergeArray(
                shared.ofType(Action.ChooseAction.class).compose(choose)));
    }
}
