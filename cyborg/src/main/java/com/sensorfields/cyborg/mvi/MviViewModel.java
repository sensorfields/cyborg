package com.sensorfields.cyborg.mvi;

import io.reactivex.Observable;

public interface MviViewModel<I extends MviIntent, S extends MviViewState> {

    void process(Observable<I> intents);

    Observable<S> viewStates();
}
