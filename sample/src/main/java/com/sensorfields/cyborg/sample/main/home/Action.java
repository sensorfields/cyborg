package com.sensorfields.cyborg.sample.main.home;

import com.google.auto.value.AutoValue;
import com.sensorfields.cyborg.mvi.MviAction;
import com.sensorfields.cyborg.navigator.Transaction;
import com.sensorfields.cyborg.navigator.TransactionAction;
import com.sensorfields.cyborg.sample.beer.list.BeerListScreen;

interface Action extends MviAction {

    @AutoValue
    abstract class RenderAction implements Action {

        static RenderAction create() {
            return new AutoValue_Action_RenderAction();
        }
    }

    @AutoValue
    abstract class BeerListAction implements Action, TransactionAction {

        static BeerListAction create() {
            return new AutoValue_Action_BeerListAction(Transaction.push(BeerListScreen.create()));
        }
    }
}
