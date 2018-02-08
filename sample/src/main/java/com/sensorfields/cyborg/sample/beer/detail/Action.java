package com.sensorfields.cyborg.sample.beer.detail;

import com.google.auto.value.AutoValue;
import com.sensorfields.cyborg.mvi.MviAction;
import com.sensorfields.cyborg.navigator.Transaction;
import com.sensorfields.cyborg.navigator.TransactionAction;
import com.sensorfields.cyborg.sample.Intents;

import static com.sensorfields.cyborg.sample.Constants.RC_BEER_DETAIL_CHOOSE;

interface Action extends MviAction {

    @AutoValue
    abstract class ChooseAction implements Action,
            TransactionAction<Transaction.ActivityForResultTransaction> {

        static ChooseAction create() {
            return new AutoValue_Action_ChooseAction(Transaction
                    .activityForResult(RC_BEER_DETAIL_CHOOSE, Intents.getContent(), null));
        }
    }
}
