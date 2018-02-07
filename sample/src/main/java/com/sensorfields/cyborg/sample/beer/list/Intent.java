package com.sensorfields.cyborg.sample.beer.list;

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
    abstract class ChooseIntent implements Intent {

        static ChooseIntent create() {
            return new AutoValue_Intent_ChooseIntent();
        }
    }

    @AutoValue
    abstract class DetailIntent implements Intent {

        static DetailIntent create() {
            return new AutoValue_Intent_DetailIntent();
        }
    }
}
