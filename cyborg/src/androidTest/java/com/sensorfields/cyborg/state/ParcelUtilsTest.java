package com.sensorfields.cyborg.state;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    }
}
