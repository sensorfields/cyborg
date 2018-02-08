package com.sensorfields.cyborg.sample.beer.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sensorfields.cyborg.ViewDisposables;
import com.sensorfields.cyborg.mvi.MviView;
import com.sensorfields.cyborg.sample.Application;
import com.sensorfields.cyborg.sample.R;

import io.reactivex.Observable;

import static com.jakewharton.rxbinding2.view.RxView.clicks;

public final class BeerListView extends LinearLayout implements MviView<Intent, ViewState> {

    private final ViewDisposables disposables = new ViewDisposables();
    private final BeerListViewModel viewModel;

    private final Button chooseButton;
    private final Button detailButton;

    public BeerListView(Context context) {
        this(context, null);
    }

    public BeerListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BeerListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BeerListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        viewModel = Application.component(context).navigator().viewModel(BeerListViewModel.class);
        setOrientation(VERTICAL);
        inflate(context, R.layout.beer_list, this);
        chooseButton = findViewById(R.id.beerListChooseButton);
        detailButton = findViewById(R.id.beerListDetailButton);
    }

    private Observable<Intent.InitialIntent> initialIntent() {
        return Observable.just(Intent.InitialIntent.create());
    }

    private Observable<Intent.ChooseIntent> chooseIntent() {
        return clicks(chooseButton).map(ignored -> Intent.ChooseIntent.create());
    }

    private Observable<Intent.DetailIntent> detailIntent() {
        return clicks(detailButton).map(ignored -> Intent.DetailIntent.create());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<Intent> intents() {
        return Observable.mergeArray(initialIntent(), chooseIntent(), detailIntent());
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
