package com.sensorfields.cyborg.sample;

import android.content.Intent;

public final class Intents {

    public static Intent getContent() {
        return Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT)
                .setType("*/*")
                .addCategory(Intent.CATEGORY_OPENABLE)
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true), null);
    }

    private Intents() {}
}
