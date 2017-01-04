package com.sensorfields.cyborg.task;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;

/**
 * When ScreenChangeListener or something is on, it should call detach and remove for every task on the screen
 */
public class TaskManager {

    private final Map<String, SingleTask<?>> tasks = new HashMap<>();

    public <T> void attachSingle(String id, Consumer<T> onSuccess) {
        this.<T>getOrCreateSingleTask(id).attach(onSuccess);
    }

    public <T> void start(String id, Single<T> single) {
        this.<T>getOrCreateSingleTask(id).start(single);
    }

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
