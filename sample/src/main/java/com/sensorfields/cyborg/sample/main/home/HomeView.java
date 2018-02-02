package com.sensorfields.cyborg.sample.main.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.sensorfields.cyborg.navigator.Navigator;
import com.sensorfields.cyborg.sample.Application;
import com.sensorfields.cyborg.sample.R;
import com.sensorfields.cyborg.sample.beer.list.BeerListScreen;

public final class HomeView extends AppCompatTextView {

    private final Navigator navigator;

    public HomeView(Context context) {
        this(context, null);
    }

    public HomeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public HomeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        navigator = Application.component(context).navigator();
        setGravity(Gravity.CENTER);
        setText(R.string.main_home_text);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnClickListener(ignored -> navigator.push(BeerListScreen.create()));
    }

    @Override
    protected void onDetachedFromWindow() {
        setOnClickListener(null);
        super.onDetachedFromWindow();
    }
}
