package com.sensorfields.cyborg.sample.beer.detail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.sensorfields.cyborg.ViewDisposables;
import com.sensorfields.cyborg.mvi.MviView;
import com.sensorfields.cyborg.sample.Application;
import com.sensorfields.cyborg.sample.R;

import io.reactivex.Observable;

public final class BeerDetailView extends AppCompatTextView implements MviView<Intent, ViewState> {

    private final ViewDisposables disposables = new ViewDisposables();
    private final BeerDetailViewModel viewModel;

    public BeerDetailView(Context context) {
        this(context, null);
    }

    public BeerDetailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BeerDetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewModel = Application.component(context).navigator().viewModel(BeerDetailViewModel.class);
        setGravity(Gravity.CENTER);
        setText(R.string.beer_detail_text);
    }

    private Observable<Intent.InitialIntent> initialIntent() {
        return Observable.just(Intent.InitialIntent.create());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<Intent> intents() {
        return Observable.mergeArray(initialIntent());
    }

    @Override
    public void render(ViewState state) {
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        disposables.attach();
        disposables.add(viewModel.viewStates().subscribe(this::render));
        viewModel.process(intents());
    }

    @Override
    protected void onDetachedFromWindow() {
        disposables.detach();
        super.onDetachedFromWindow();
    }
}
