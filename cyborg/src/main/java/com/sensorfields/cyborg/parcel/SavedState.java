package com.sensorfields.cyborg.parcel;

import android.os.Parcel;
import android.os.Parcelable;

public final class SavedState implements Parcelable {

    public interface StateAware {
        void onSaveState(Parcel state);
        void onRestoreState(Parcel state);
    }

    public static Parcelable onSaveInstanceState(StateAware stateAware, Parcelable superState) {
        Parcel parcel = Parcel.obtain();
        stateAware.onSaveState(parcel);
        parcel.setDataPosition(0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return new SavedState(superState, bytes);
    }

    public static Parcelable onRestoreInstanceState(StateAware stateAware, Parcelable state) {
        SavedState savedState = (SavedState) state;
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(savedState.state, 0, savedState.state.length);
        parcel.setDataPosition(0);
        stateAware.onRestoreState(parcel);
        parcel.recycle();
        return savedState.superState;
    }

    private final Parcelable superState;
    private final byte[] state;

    private SavedState(Parcelable superState, byte[] state) {
        this.superState = superState;
        this.state = state;
    }

    private SavedState(Parcel source, ClassLoader loader) {
        superState = source.readParcelable(loader);
        state = source.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(superState, flags);
        dest.writeByteArray(state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
        @Override
        public SavedState createFromParcel(Parcel source, ClassLoader loader) {
            return new SavedState(source, loader);
        }
        @Override
        public SavedState createFromParcel(Parcel source) {
            return createFromParcel(source, null);
        }
        @Override
        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    };
}
