package com.sensorfields.cyborg.sample.beer.list;

import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.navigator.Transaction;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

final class Processor implements ObservableTransformer<Action, Result> {

    private final ObservableTransformer<Action.RenderAction, Result.RenderResult> render;

    private final ObservableTransformer<Action.ChooseAction, Result.ChooseResult> choose;

    private final ObservableTransformer<Action.DetailAction, Result.RenderResult> detail;

    @Inject Processor(Navigator navigator) {
        render = upstream -> upstream.map(action -> Result.RenderResult.create());

        choose = upstream -> upstream.flatMap(action -> {
            Transaction.ActivityForResultTransaction transaction =
                    (Transaction.ActivityForResultTransaction) action.transaction();
            navigator.execute(transaction);

            return Observable.empty();
//            return navigator.activityResults()
//                    .filter(activityResult ->
//                            activityResult.requestCode() == transaction.requestCode())
//                    .map(activityResult ->
//                            Result.ChooseResult
//                                    .create(activityResult.resultCode() == Activity.RESULT_OK));
        });

        detail = upstream -> upstream
                .filter(action -> {
                    navigator.execute(action.transaction());
                    return false;
                })
                .map(action -> Result.RenderResult.create());
    }

    @SuppressWarnings("unchecked")
    @Override
    public ObservableSource<Result> apply(Observable<Action> upstream) {
        return upstream.publish(shared -> Observable.mergeArray(
                shared.ofType(Action.RenderAction.class).compose(render),
                shared.ofType(Action.ChooseAction.class).compose(choose),
                shared.ofType(Action.DetailAction.class).compose(detail)));
    }
}
