package com.sensorfields.cyborg.parcel;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class SavedStateTest {

    private SavedState.StateAware stateAware;

    @Before
    public void setup() {
        stateAware = mock(SavedState.StateAware.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Parcel state = invocation.getArgument(0);
                state.writeInt(5);
                state.writeString("testing");
                state.writeLong(7L);
                return null;
            }
        }).when(stateAware).onSaveState(isA(Parcel.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Parcel state = invocation.getArgument(0);
                assertEquals(5, state.readInt());
                assertEquals("testing", state.readString());
                assertEquals(7L, state.readLong());
                return null;
            }
        }).when(stateAware).onRestoreState(isA(Parcel.class));
    }

    @Test
    public void parcelableSaveAndRestore() {
        ParcelUuid superState = new ParcelUuid(UUID.randomUUID());

        Parcelable savedState = SavedState.onSaveInstanceState(stateAware, superState);
        Parcelable newSuperState = SavedState.onRestoreInstanceState(stateAware, savedState);

        assertEquals(superState, newSuperState);
    }

    @Test
    public void parcelableSaveAndRestoreThroughParcel() {
        ParcelUuid superState = new ParcelUuid(UUID.randomUUID());

        Parcelable savedState = SavedState.onSaveInstanceState(stateAware, superState);

        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(savedState, 0);
        parcel.setDataPosition(0);
        Parcelable newSavedState = parcel.readParcelable(getClass().getClassLoader());
        parcel.recycle();

        Parcelable newSuperState = SavedState.onRestoreInstanceState(stateAware, newSavedState);

        assertEquals(superState, newSuperState);
    }
}
