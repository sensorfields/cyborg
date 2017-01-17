package com.sensorfields.cyborg.sample.task;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sensorfields.cyborg.state.Stateful;
import com.sensorfields.cyborg.state.ViewSavedState;
import com.sensorfields.cyborg.task.TaskManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SingleTaskListView extends SwipeRefreshLayout implements
        SwipeRefreshLayout.OnRefreshListener, Stateful {

    // fake DI
    private static final TaskManager taskManager = new TaskManager();
    private static final ItemRepository itemRepository = new ItemRepository();

    private final ItemAdapter adapter;

    private String findItemsTaskId = UUID.randomUUID().toString();
    private boolean firstAttach = true;

    public SingleTaskListView(Context context) {
        this(context, null);
    }

    public SingleTaskListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnRefreshListener(this);
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setId(generateViewId());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter = new ItemAdapter());
        addView(recyclerView);
    }

    @Override
    public void onRefresh() {
        findItems();
    }

    void findItems() {
        taskManager.start(findItemsTaskId, itemRepository.findItems());
    }

    void setItems(List<String> items) {
        adapter.setData(items);
        setRefreshing(false);
    }

    void setError(Throwable e) {
        Timber.e(e, "setError yo yoo");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        taskManager.attachSingle(findItemsTaskId, this::setItems, this::setError);
        if (firstAttach) {
            findItems();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        taskManager.detach(findItemsTaskId);
        super.onDetachedFromWindow();
    }

    @Override
    public void onSaveState(Parcel state) {
        state.writeString(findItemsTaskId);
        state.writeStringList(adapter.getData());
    }

    @Override
    public void onRestoreState(Parcel state) {
        findItemsTaskId = state.readString();
        adapter.setData(state.createStringArrayList());

        firstAttach = false;
    }

    static class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private final List<String> data = new ArrayList<>();

        void setData(List<String> data) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        List<String> getData() {
            return data;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_selectable_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ItemViewHolder extends RecyclerView.ViewHolder {
            final TextView textView;
            ItemViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }

    static class ItemRepository {
        Single<List<String>> findItems() {
            return Single
                    .fromCallable(() -> {
                        SystemClock.sleep(5000);
                        return Arrays.asList("One " + Math.random(), "Two " + Math.random(),
                                "Three" + Math.random());
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return ViewSavedState.onSaveInstanceState(this, super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(ViewSavedState.onRestoreInstanceState(this, state));
    }
}
