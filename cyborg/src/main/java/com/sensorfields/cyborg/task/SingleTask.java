package com.sensorfields.cyborg.task;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.Subject;

class SingleTask<T> {

    private final TaskManager taskManager;
    private final String id;

    private final Subject<T> subject = AsyncSubject.create();

    private Consumer<T> onSuccess;
    private Disposable disposable;

    private boolean started = false;

    SingleTask(TaskManager taskManager, String id) {
        this.taskManager = taskManager;
        this.id = id;
    }

    void attach(final Consumer<T> onSuccess) {
        if (isAttached()) {
            throw new IllegalStateException("Task has to be detached before it can be attached");
        }
        this.onSuccess = onSuccess;
        disposable = subject
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        terminate();
                    }
                })
                .singleOrError().subscribe(onSuccess);
    }

    void detach() {
        onSuccess = null;
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
        taskManager.attachSingle(id, onSuccess);
        detach();
    }

    private boolean isAttached() {
        return onSuccess != null;
    }

    private boolean isStarted() {
        return started;
    }
}
