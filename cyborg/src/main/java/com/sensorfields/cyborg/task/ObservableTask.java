package com.sensorfields.cyborg.task;

import hu.akarnokd.rxjava2.operators.FlowableTransformers;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

final class ObservableTask<T> extends Task {

    private final TaskManager taskManager;
    private final String id;

    private final Subject<T> subject = PublishSubject.create();
    private final PublishProcessor<Boolean> valve = PublishProcessor.create();

    private AttachInfo<T> attachInfo;
    private Disposable disposable;

    private boolean started = false;

    ObservableTask(TaskManager taskManager, String id) {
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
                }));
        valve.onNext(true);
    }

    @Override
    void detach() {
        valve.onNext(false);
        attachInfo = null;
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    void start(Observable<T> observable) {
        if (!isAttached()) {
            throw new IllegalStateException("Task has to be attached before it can be started");
        }
        if (isStarted()) {
            return;
        }
        started = true;
        observable
                .toFlowable(BackpressureStrategy.MISSING)
                .compose(FlowableTransformers.<T>valve(valve))
                .toObservable()
                .subscribe(subject);
    }

    private void terminate() {
        taskManager.remove(id);
        taskManager.attachObservable(id, attachInfo);
        detach();
    }

    private boolean isAttached() {
        return attachInfo != null;
    }

    private boolean isStarted() {
        return started;
    }

    static final class AttachInfo<T> {

        final Consumer<T> onNext;

        AttachInfo(Consumer<T> onNext) {
            this.onNext = onNext;
        }

        Disposable subscribe(Observable<T> observable) {
            return observable.subscribe(onNext);
        }
    }
}
