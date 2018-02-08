package com.sensorfields.cyborg.navigator;

public interface TransactionAction<T extends Transaction> {

    T transaction();
}
