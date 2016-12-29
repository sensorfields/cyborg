package com.sensorfields.cyborg.state;

import android.os.Parcel;

public final class ParcelUtils {

    public static void writeBoolean(Parcel dest, boolean value) {
        dest.writeInt(value ? 1 : 0);
    }

    public static boolean readBoolean(Parcel source) {
        return source.readInt() != 0;
    }
}
