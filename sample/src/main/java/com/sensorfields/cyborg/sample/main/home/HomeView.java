package com.sensorfields.cyborg.sample.main.home;

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

import static com.jakewharton.rxbinding2.view.RxView.clicks;

public final class HomeView extends AppCompatTextView implements MviView<Intent, ViewState> {

    private final ViewDisposables disposables = new ViewDisposables();
    private final HomeViewModel viewModel;

    public HomeView(Context context) {
        this(context, null);
    }

    public HomeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public HomeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewModel = Application.component(context).navigator().viewModel(HomeViewModel.class);
        setGravity(Gravity.CENTER);
        setText(R.string.main_home_text);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<Intent> intents() {
        return Observable.mergeArray(initialIntent(), beerListIntent());
    }

    @Override
    public void render(ViewState state) {
    }

    private Observable<Intent.InitialIntent> initialIntent() {
        return Observable.just(Intent.InitialIntent.create());
    }

    private Observable<Intent.BeerListIntent> beerListIntent() {
        return clicks(this).map(ignored -> Intent.BeerListIntent.create());
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
