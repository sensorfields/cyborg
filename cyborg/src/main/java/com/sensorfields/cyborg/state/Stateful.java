package com.sensorfields.cyborg.state;

import android.os.Parcel;

/**
 * Save state to {@link Parcel} instead of a bundle or custom {@link android.os.Parcelable} object.
 *
 * Bundles need keys (defined as constants) and extending {@link android.view.View.BaseSavedState}
 * needs too much boilerplate.
 */
public interface Stateful {

    /**
     * Save data to {@link Parcel}.
     */
    void onSaveState(Parcel state);

    /**
     * Restore data from {@link Parcel}.
     */
    void onRestoreState(Parcel state);
}
