package com.sensorfields.cyborg.sample.beer.list;

import com.google.auto.value.AutoValue;
import com.sensorfields.cyborg.mvi.MviAction;

interface Action extends MviAction {

    @AutoValue
    abstract class RenderAction implements Action {

        static RenderAction create() {
            return new AutoValue_Action_RenderAction();
        }
    }
}
