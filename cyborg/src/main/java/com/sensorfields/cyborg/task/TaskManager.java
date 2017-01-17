package com.sensorfields.cyborg.task;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * When ScreenChangeListener or something is on, it should call detach and remove for every task on the screen
 */
public class TaskManager {

    private final Map<String, SingleTask<?>> tasks = new HashMap<>();

    // Single

    public <T> void attachSingle(String id, Consumer<T> onSuccess) {
        attachSingle(id, new SingleTask.AttachInfo<>(onSuccess));
    }

    public <T> void attachSingle(String id, Consumer<T> onSuccess, Consumer<Throwable> onError) {
        attachSingle(id, new SingleTask.AttachInfo<>(onSuccess, onError));
    }

    public <T> void attachSingle(String id, BiConsumer<T, Throwable> onCallback) {
        attachSingle(id, new SingleTask.AttachInfo<>(onCallback));
    }

    <T> void attachSingle(String id, SingleTask.AttachInfo<T> attachInfo) {
        this.<T>getOrCreateSingleTask(id).attach(attachInfo);
    }

    public <T> void start(String id, Single<T> single) {
        this.<T>getOrCreateSingleTask(id).start(single);
    }

    // General

    public void detach(String id) {
        getOrCreateSingleTask(id).detach();
    }

    void remove(String id) {
        tasks.remove(id);
    }

    @SuppressWarnings("unchecked")
    private <T> SingleTask<T> getOrCreateSingleTask(String id) {
        SingleTask<T> task = (SingleTask<T>) tasks.get(id);
        if (task == null) {
            task = new SingleTask<>(this, id);
            tasks.put(id, task);
        }
        return task;
    }
}
