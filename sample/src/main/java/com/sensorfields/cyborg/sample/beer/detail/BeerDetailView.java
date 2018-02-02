package com.sensorfields.cyborg.sample.beer.detail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.sensorfields.cyborg.sample.R;

public final class BeerDetailView extends AppCompatTextView {

    public BeerDetailView(Context context) {
        this(context, null);
    }

    public BeerDetailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BeerDetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
        setText(R.string.beer_detail_text);
    }
}
