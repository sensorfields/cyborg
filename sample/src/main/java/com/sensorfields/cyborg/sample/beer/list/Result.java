package com.sensorfields.cyborg.sample.beer.list;

import com.google.auto.value.AutoValue;
import com.sensorfields.cyborg.mvi.MviResult;

interface Result extends MviResult<ViewState> {

    @AutoValue
    abstract class RenderResult implements Result {

        @Override
        public ViewState reduce(ViewState state) {
            return state;
        }

        static RenderResult create() {
            return new AutoValue_Result_RenderResult();
        }
    }

    @AutoValue
    abstract class ChooseResult implements Result {

        abstract boolean success();

        @Override
        public ViewState reduce(ViewState state) {
            return state;
        }

        static ChooseResult create(boolean success) {
            return new AutoValue_Result_ChooseResult(success);
        }
    }
}
