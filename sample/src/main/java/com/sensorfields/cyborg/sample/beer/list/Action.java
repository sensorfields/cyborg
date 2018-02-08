package com.sensorfields.cyborg.sample.beer.list;

import com.google.auto.value.AutoValue;
import com.sensorfields.cyborg.mvi.MviAction;
import com.sensorfields.cyborg.navigator.Transaction;
import com.sensorfields.cyborg.navigator.TransactionAction;
import com.sensorfields.cyborg.sample.Intents;
import com.sensorfields.cyborg.sample.beer.detail.BeerDetailScreen;

import static com.sensorfields.cyborg.sample.Constants.RC_BEER_LIST_CHOOSE;

interface Action extends MviAction {

    @AutoValue
    abstract class RenderAction implements Action {

        static RenderAction create() {
            return new AutoValue_Action_RenderAction();
        }
    }

    @AutoValue
    abstract class ChooseAction implements Action,
            TransactionAction<Transaction.ActivityForResultTransaction> {

        static ChooseAction create() {
            return new AutoValue_Action_ChooseAction(Transaction
                    .activityForResult(RC_BEER_LIST_CHOOSE, Intents.getContent(), null));
        }
    }

    @AutoValue
    abstract class DetailAction implements Action, TransactionAction {

        static DetailAction create() {
            return new AutoValue_Action_DetailAction(Transaction.push(BeerDetailScreen.create()));
        }
    }
}
