package com.sensorfields.cyborg.mvi;

import android.arch.lifecycle.ViewModel;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

public abstract class BaseMviViewModel<
        I extends MviIntent,
        S extends MviViewState,
        A extends MviAction,
        R extends MviResult<S>> extends ViewModel implements MviViewModel<I, S> {

    private final Subject<I> intentSubject;
    private final Observable<S> viewStateObservable;

    protected BaseMviViewModel(ObservableTransformer<A, R> processor) {
        intentSubject = PublishSubject.create();
        viewStateObservable = intentSubject
                .compose(this::initialIntentFilter)
                .doOnNext(intent -> Timber.d("Intent: %s", intent))
                .map(this::action)
                .doOnNext(action -> Timber.d("Action: %s", action))
                .compose(processor)
                .doOnNext(result -> Timber.d("Result: %s", result))
                .scan(initialViewState(), (viewState, result) -> result.reduce(viewState))
                .doOnNext(viewState -> Timber.d("ViewState: %s", viewState))
                .replay(1)
                .autoConnect(0);
    }

    @Override
    public void process(Observable<I> intents) {
        intents.subscribe(intentSubject);
    }

    @Override
    public Observable<S> viewStates() {
        return viewStateObservable;
    }

    protected abstract Class<? extends I> initialIntentType();

    protected abstract A action(I intent);

    protected abstract S initialViewState();

    private ObservableSource<I> initialIntentFilter(Observable<I> intents) {
        Class<? extends I> initialIntentType = initialIntentType();
        return intents.publish(shared -> Observable.merge(
                shared.ofType(initialIntentType).take(1),
                shared.filter(intent -> !initialIntentType.isInstance(intent))));
    }
}
