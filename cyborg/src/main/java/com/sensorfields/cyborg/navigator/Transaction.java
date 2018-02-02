package com.sensorfields.cyborg.navigator;

import com.google.auto.value.AutoValue;

public abstract class Transaction {

    public static RootTransaction root(Screen screen) {
        return RootTransaction.create(screen);
    }

    public static PushTransaction push(Screen screen) {
        return PushTransaction.create(screen);
    }

    @AutoValue
    public abstract static class RootTransaction extends Transaction {

        public abstract Screen screen();

        static RootTransaction create(Screen screen) {
            return new AutoValue_Transaction_RootTransaction(screen);
        }
    }

    @AutoValue
    public abstract static class PushTransaction extends Transaction {

        public abstract Screen screen();

        static PushTransaction create(Screen screen) {
            return new AutoValue_Transaction_PushTransaction(screen);
        }
    }
}
