package com.sensorfields.cyborg.sample.beer.detail;

import com.google.auto.value.AutoValue;
import com.sensorfields.cyborg.mvi.MviIntent;

interface Intent extends MviIntent {

    @AutoValue
    abstract class InitialIntent implements Intent {

        static InitialIntent create() {
            return new AutoValue_Intent_InitialIntent();
        }
    }
}
