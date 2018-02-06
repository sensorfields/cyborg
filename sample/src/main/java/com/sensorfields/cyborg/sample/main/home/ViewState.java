package com.sensorfields.cyborg.sample.main.home;

import com.google.auto.value.AutoValue;
import com.sensorfields.cyborg.mvi.MviViewState;

@AutoValue
abstract class ViewState implements MviViewState {

    static ViewState initial() {
        return new AutoValue_ViewState();
    }
}
