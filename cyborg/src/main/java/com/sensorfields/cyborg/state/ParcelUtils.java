package com.sensorfields.cyborg.state;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

@SuppressWarnings("ConstantConditions")
public final class ParcelUtils {

    public static void writeBoolean(Parcel dest, boolean value) {
        dest.writeInt(value ? 1 : 0);
    }

    public static boolean readBoolean(Parcel source) {
        return source.readInt() != 0;
    }

    public static void writeInteger(Parcel dest, @Nullable Integer value) {
        if (!writeNullable(dest, value)) {
            dest.writeInt(value);
        }
    }

    @Nullable public static Integer readInteger(Parcel source) {
        return readBoolean(source) ? null : source.readInt();
    }

    public static void writeLong(Parcel dest, @Nullable Long value) {
        if (!writeNullable(dest, value)) {
            dest.writeLong(value);
        }
    }

    @Nullable public static Long readLong(Parcel source) {
        return readBoolean(source) ? null : source.readLong();
    }

    public static void writeParcelable(Parcel dest, Parcelable value, int flags) {
        if (!writeNullable(dest, value)) {
            value.writeToParcel(dest, flags);
        }
    }

    @Nullable public static <T extends Parcelable> T readParcelable(Parcel source,
                                                          Parcelable.Creator<T> creator) {
        return readBoolean(source) ? null : creator.createFromParcel(source);
    }

    private static boolean writeNullable(Parcel dest, Object value) {
        boolean isNull = value == null;
        writeBoolean(dest, isNull);
        return isNull;
    }
}
