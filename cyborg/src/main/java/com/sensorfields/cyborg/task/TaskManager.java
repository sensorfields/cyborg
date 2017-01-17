package com.sensorfields.cyborg.task;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * When ScreenChangeListener or something is on, it should call detach and remove for every task on the screen
 */
public class TaskManager {

    private final Map<String, Task> tasks = new HashMap<>();

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

    // Completable

    public void attachCompletable(String id, Action onComplete) {
        attachCompletable(id, new CompletableTask.AttachInfo(onComplete));
    }

    public void attachCompletable(String id, Action onComplete, Consumer<Throwable> onError) {
        attachCompletable(id, new CompletableTask.AttachInfo(onComplete, onError));
    }

    void attachCompletable(String id, CompletableTask.AttachInfo attachInfo) {
        getOrCreateCompletableTask(id).attach(attachInfo);
    }

    public void start(String id, Completable completable) {
        getOrCreateCompletableTask(id).start(completable);
    }

    // General

    public void detach(String id) {
        if (tasks.containsKey(id)) {
            tasks.get(id).detach();
        }
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

    private CompletableTask getOrCreateCompletableTask(String id) {
        CompletableTask task = (CompletableTask) tasks.get(id);
        if (task == null) {
            task = new CompletableTask(this, id);
            tasks.put(id, task);
        }
        return task;
    }
}
