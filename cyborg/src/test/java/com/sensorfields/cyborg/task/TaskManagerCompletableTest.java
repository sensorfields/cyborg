package com.sensorfields.cyborg.task;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TaskManagerCompletableTest {

    private TaskManager taskManager;
    private String id;
    private Subject<Object> subject;
    private Completable completable;
    private Action onComplete;

    @Before
    public void setup() {
        taskManager = new TaskManager();
        id = UUID.randomUUID().toString();
        subject = PublishSubject.create();
        completable = Completable.fromObservable(subject);
        onComplete = mock(Action.class);
    }

    @Test
    public void onComplete() throws Exception {
        // we get the value
        taskManager.attachCompletable(id, onComplete);
        taskManager.start(id, completable);
        verify(onComplete, never()).run();
        subject.onComplete();
        verify(onComplete).run();

        // we get new value if starting again
        subject = PublishSubject.create();
        completable = Completable.fromObservable(subject);
        taskManager.start(id, completable);
        subject.onComplete();
        verify(onComplete, times(2)).run();

        // we do not get the value again if we detach and attach
        onComplete = mock(Action.class);
        taskManager.detach(id);
        taskManager.attachCompletable(id, onComplete);
        verify(onComplete, never()).run();

        // we don't get the value if we are detached, but get the value after attach
        subject = PublishSubject.create();
        completable = Completable.fromObservable(subject);
        taskManager.start(id, completable);
        taskManager.detach(id);
        subject.onComplete();
        verify(onComplete, never()).run();
        taskManager.attachCompletable(id, onComplete);
        verify(onComplete).run();
    }

    @Test
    public void onComplete_attachWhenAttached() throws Exception {
        taskManager.attachCompletable(id, onComplete);
        try {
            taskManager.attachCompletable(id, onComplete);
            fail("Expected IllegalStateException when attaching a task with ID that is already attached");
        } catch (IllegalStateException ignored) {}

        taskManager.start(id, completable);
        try {
            taskManager.attachCompletable(id, onComplete);
            fail("Expected IllegalStateException when attaching a task with ID that is already attached and started");
        } catch (IllegalStateException ignored) {}

        subject.onComplete();

        try {
            taskManager.attachCompletable(id, onComplete);
            fail("Expected IllegalStateException when attaching a task with ID that is already attached and completed");
        } catch (IllegalStateException ignored) {}
    }

    @Test
    public void onSuccess_startBeforeAttach() {
        try {
            taskManager.start(id, completable);
            fail("Expected IllegalStateException when starting a task with not attached ID");
        } catch (IllegalStateException ignored) {}
    }

    @Test
    public void onSuccess_startAfterDetach() {
        taskManager.attachCompletable(id, onComplete);
        taskManager.detach(id);
        try {
            taskManager.start(id, completable);
            fail("Expected IllegalStateException when starting a task with detached ID");
        } catch (IllegalStateException ignored) {}
    }

    @Test
    public void onSuccess_startAfterAlreadyStarted() throws Exception {
        taskManager.attachCompletable(id, onComplete);
        taskManager.start(id, completable);
        verify(onComplete, never()).run();

        // if old completable is not completed, just ignore new completable
        Subject<Object> subject2 = PublishSubject.create();
        Completable completable2 = Completable.fromObservable(subject2);
        taskManager.start(id, completable2);
        assertFalse(subject2.hasObservers());
        subject2.onComplete();
        verify(onComplete, never()).run();
        subject.onComplete();
        verify(onComplete).run();
    }

    @Test
    public void onSuccess_startAfterCompleted() throws Exception {
        taskManager.attachCompletable(id, onComplete);
        taskManager.start(id, completable);
        verify(onComplete, never()).run();
        subject.onComplete();
        verify(onComplete).run();

        Subject<Object> subject2 = PublishSubject.create();
        Completable completable2 = Completable.fromObservable(subject2);
        taskManager.start(id, completable2);
        subject2.onComplete();
        verify(onComplete, times(2)).run();
    }
}
