package com.sensorfields.cyborg.task;

import android.support.annotation.Nullable;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.Subject;

class SingleTask<T> {

    private final TaskManager taskManager;
    private final String id;

    private final Subject<T> subject = AsyncSubject.create();

    private AttachInfo<T> attachInfo;
    private Disposable disposable;

    private boolean started = false;

    SingleTask(TaskManager taskManager, String id) {
        this.taskManager = taskManager;
        this.id = id;
    }

    void attach(AttachInfo<T> attachInfo) {
        if (isAttached()) {
            throw new IllegalStateException("Task has to be detached before it can be attached");
        }
        this.attachInfo = attachInfo;
        disposable = attachInfo.subscribe(subject
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        terminate();
                    }
                })
                .singleOrError());
    }

    void detach() {
        attachInfo = null;
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    void start(Single<T> single) {
        if (!isAttached()) {
            throw new IllegalStateException("Task has to be attached before it can be started");
        }
        if (isStarted()) {
            return;
        }
        started = true;
        single.toObservable().subscribe(subject);
    }

    private void terminate() {
        taskManager.remove(id);
        taskManager.attachSingle(id, attachInfo);
        detach();
    }

    private boolean isAttached() {
        return attachInfo != null;
    }

    private boolean isStarted() {
        return started;
    }

    static final class AttachInfo<T> {

        @Nullable final Consumer<T> onSuccess;
        @Nullable final Consumer<Throwable> onError;
        @Nullable final BiConsumer<T, Throwable> onCallback;

        AttachInfo(Consumer<T> onSuccess) {
            this(onSuccess, null, null);
        }

        AttachInfo(Consumer<T> onSuccess, Consumer<Throwable> onError) {
            this(onSuccess, onError, null);
        }

        AttachInfo(BiConsumer<T, Throwable> onCallback) {
            this(null, null, onCallback);
        }

        private AttachInfo(@Nullable Consumer<T> onSuccess, @Nullable Consumer<Throwable> onError,
                           @Nullable BiConsumer<T, Throwable> onCallback) {
            this.onSuccess = onSuccess;
            this.onError = onError;
            this.onCallback = onCallback;
        }

        Disposable subscribe(Single<T> single) {
            if (onCallback != null) {
                return single.subscribe(onCallback);
            } else if (onSuccess != null && onError == null) {
                return single.subscribe(onSuccess);
            } else if (onSuccess != null) {
                return single.subscribe(onSuccess, onError);
            }
            throw new IllegalArgumentException("Please provide valid callbacks");
        }
    }
}
