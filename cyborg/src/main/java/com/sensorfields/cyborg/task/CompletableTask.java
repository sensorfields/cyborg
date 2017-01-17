package com.sensorfields.cyborg.task;

import android.support.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.Subject;

final class CompletableTask extends Task {

    private final TaskManager taskManager;
    private final String id;

    private final Subject<Object> subject = AsyncSubject.create();

    private AttachInfo attachInfo;
    private Disposable disposable;

    private boolean started = false;

    CompletableTask(TaskManager taskManager, String id) {
        this.taskManager = taskManager;
        this.id = id;
    }

    void attach(AttachInfo attachInfo) {
        if (isAttached()) {
            throw new IllegalStateException("Task has to be detached before it can be attached");
        }
        this.attachInfo = attachInfo;
        disposable = attachInfo.subscribe(Completable.fromObservable(subject
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        terminate();
                    }
                })));
    }

    @Override
    void detach() {
        attachInfo = null;
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    void start(Completable completable) {
        if (!isAttached()) {
            throw new IllegalStateException("Task has to be attached before it can be started");
        }
        if (isStarted()) {
            return;
        }
        started = true;
        completable.toObservable().subscribe(subject);
    }

    private void terminate() {
        taskManager.remove(id);
        taskManager.attachCompletable(id, attachInfo);
        detach();
    }

    private boolean isAttached() {
        return attachInfo != null;
    }

    private boolean isStarted() {
        return started;
    }

    static final class AttachInfo {

        final Action onComplete;
        @Nullable final Consumer<Throwable> onError;

        AttachInfo(Action onComplete) {
            this(onComplete, null);
        }

        AttachInfo(Action onComplete, @Nullable Consumer<Throwable> onError) {
            this.onComplete = onComplete;
            this.onError = onError;
        }

        Disposable subscribe(Completable completable) {
            if (onError == null) {
                return completable.subscribe(onComplete);
            } else {
                return completable.subscribe(onComplete, onError);
            }
        }
    }
}
