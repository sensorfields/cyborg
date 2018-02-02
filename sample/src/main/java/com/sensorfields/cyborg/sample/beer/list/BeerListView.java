package com.sensorfields.cyborg.sample.beer.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.sample.Application;
import com.sensorfields.cyborg.sample.R;
import com.sensorfields.cyborg.sample.beer.detail.BeerDetailScreen;

public final class BeerListView extends AppCompatTextView {

    private final Navigator navigator;

    public BeerListView(Context context) {
        this(context, null);
    }

    public BeerListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BeerListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        navigator = Application.component(context).navigator();
        setGravity(Gravity.CENTER);
        setText(R.string.beer_list_text);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnClickListener(ignored -> navigator.push(BeerDetailScreen.create()));
    }

    @Override
    protected void onDetachedFromWindow() {
        setOnClickListener(null);
        super.onDetachedFromWindow();
    }
}
