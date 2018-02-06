package com.sensorfields.cyborg.mvi;

public interface MviResult<S extends MviViewState> {

    S reduce(S state);
}
