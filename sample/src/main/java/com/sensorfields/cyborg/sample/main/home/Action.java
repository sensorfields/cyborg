package com.sensorfields.cyborg.sample.main.home;

import com.google.auto.value.AutoValue;
import com.sensorfields.cyborg.mvi.MviAction;

interface Action extends MviAction {

    @AutoValue
    abstract class RenderAction implements Action {

        static RenderAction create() {
            return new AutoValue_Action_RenderAction();
        }
    }

    @AutoValue
    abstract class BeerListAction implements Action {

        static BeerListAction create() {
            return new AutoValue_Action_BeerListAction();
        }
    }
}
