package com.sensorfields.cyborg.sample.main.home;

import com.google.auto.value.AutoValue;
import com.sensorfields.cyborg.mvi.MviIntent;

interface Intent extends MviIntent {

    @AutoValue
    abstract class InitialIntent implements Intent {

        static InitialIntent create() {
            return new AutoValue_Intent_InitialIntent();
        }
    }

    @AutoValue
    abstract class BeerListIntent implements Intent {

        static BeerListIntent create() {
            return new AutoValue_Intent_BeerListIntent();
        }
    }
}
