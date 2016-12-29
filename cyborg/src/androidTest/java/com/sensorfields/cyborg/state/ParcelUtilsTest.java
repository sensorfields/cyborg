package com.sensorfields.cyborg.state;

import android.location.Location;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ParcelUtilsTest {

    @Test
    public void booleanWriteRead() {
        Parcel parcel = Parcel.obtain();

        ParcelUtils.writeBoolean(parcel, true);
        ParcelUtils.writeBoolean(parcel, true);
        parcel.writeString("testing");
        ParcelUtils.writeBoolean(parcel, false);
        ParcelUtils.writeBoolean(parcel, true);

        parcel.setDataPosition(0);

        assertTrue(ParcelUtils.readBoolean(parcel));
        assertTrue(ParcelUtils.readBoolean(parcel));
        assertEquals("testing", parcel.readString());
        assertFalse(ParcelUtils.readBoolean(parcel));
        assertTrue(ParcelUtils.readBoolean(parcel));

        parcel.recycle();
    }

    @Test
    public void integerWriteRead() {
        Integer value1 = 3;
        Integer value2 = null;
        Integer value3 = -5;

        Parcel parcel = Parcel.obtain();

        ParcelUtils.writeInteger(parcel, value1);
        ParcelUtils.writeInteger(parcel, value2);
        ParcelUtils.writeInteger(parcel, value3);

        parcel.setDataPosition(0);

        assertEquals(Integer.valueOf(3), ParcelUtils.readInteger(parcel));
        assertNull(ParcelUtils.readInteger(parcel));
        assertEquals(Integer.valueOf(-5), ParcelUtils.readInteger(parcel));

        parcel.recycle();
    }

    @Test
    public void longWriteRead() {
        Long value1 = 3L;
        Long value2 = null;
        Long value3 = -5L;

        Parcel parcel = Parcel.obtain();

        ParcelUtils.writeLong(parcel, value1);
        ParcelUtils.writeLong(parcel, value2);
        ParcelUtils.writeLong(parcel, value3);

        parcel.setDataPosition(0);

        assertEquals(Long.valueOf(3L), ParcelUtils.readLong(parcel));
        assertNull(ParcelUtils.readLong(parcel));
        assertEquals(Long.valueOf(-5L), ParcelUtils.readLong(parcel));

        parcel.recycle();
    }

    @Test
    public void parcelableWriteRead() {
        ParcelUuid value1 = new ParcelUuid(UUID.randomUUID());
        ParcelUuid value2 = null;
        Location value3 = new Location("testing");

        Parcel parcel = Parcel.obtain();

        ParcelUtils.writeParcelable(parcel, value1, 0);
        ParcelUtils.writeParcelable(parcel, value2, 0);
        ParcelUtils.writeParcelable(parcel, value3, 0);

        parcel.setDataPosition(0);

        ParcelUuid result1 = ParcelUtils.readParcelable(parcel, ParcelUuid.CREATOR);
        ParcelUuid result2 = ParcelUtils.readParcelable(parcel, ParcelUuid.CREATOR);
        Location result3 = ParcelUtils.readParcelable(parcel, Location.CREATOR);

        assertEquals(result1, value1);
        assertNotSame(result1, value1);
        assertNull(result2);
        assertEquals(result3.getProvider(), value3.getProvider());
        assertNotSame(result3, value3);

        parcel.recycle();
    }
}
