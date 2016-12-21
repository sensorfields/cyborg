package com.sensorfields.cyborg.parcel;

import android.os.Parcel;
import android.os.Parcelable;

public final class SavedState {

    public interface StateAware {
        void onSaveState(Parcel state);
        void onRestoreState(Parcel state);
    }

    public static Parcelable onSaveInstanceState(StateAware stateAware, Parcelable superState) {
        return superState;
    }

    public static Parcelable onRestoreInstanceState(StateAware stateAware, Parcelable state) {
        return state;
    }
}
