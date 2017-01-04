package com.sensorfields.cyborg.task;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests for every attach() method
 * Tests for success and failure
 */
@SuppressWarnings({"EmptyCatchBlock","unchecked"})
public class TaskManagerSingleTest {

    private TaskManager taskManager;
    private String id;
    private Subject<String> subject;
    private Single<String> single;
    private Consumer<String> onSuccess;

    @Before
    public void setup() {
        taskManager = new TaskManager();
        id = UUID.randomUUID().toString();
        subject = PublishSubject.create();
        single = subject.singleOrError();
        onSuccess = mock(Consumer.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void onSuccess() throws Exception {
        // we get the value
        taskManager.attachSingle(id, onSuccess);
        taskManager.start(id, single);
        verify(onSuccess, never()).accept(anyString());
        onSuccess("one");
        verify(onSuccess).accept("one");

        // we get new value if starting again
        subject = PublishSubject.create();
        single = subject.singleOrError();
        taskManager.start(id, single);
        onSuccess("two");
        verify(onSuccess).accept("two");

        // we do not get the value again if we detach and attach
        onSuccess = mock(Consumer.class);
        taskManager.detach(id);
        taskManager.attachSingle(id, onSuccess);
        verify(onSuccess, never()).accept(anyString());

        // we don't get the value if we are detached, but get the value after attach
        subject = PublishSubject.create();
        single = subject.singleOrError();
        taskManager.start(id, single);
        taskManager.detach(id);
        onSuccess("three");
        verify(onSuccess, never()).accept(anyString());
        taskManager.attachSingle(id, onSuccess);
        verify(onSuccess).accept("three");
    }

    @Test
    public void onSuccess_attachWhenAttached() throws Exception {
        taskManager.attachSingle(id, onSuccess);
        try {
            taskManager.attachSingle(id, onSuccess);
            fail("Expected IllegalStateException when attaching a task with ID that is already attached");
        } catch (IllegalStateException e) {}

        taskManager.start(id, single);
        try {
            taskManager.attachSingle(id, onSuccess);
            fail("Expected IllegalStateException when attaching a task with ID that is already attached and started");
        } catch (IllegalStateException e) {}

        onSuccess("one");

        try {
            taskManager.attachSingle(id, onSuccess);
            fail("Expected IllegalStateException when attaching a task with ID that is already attached and completed");
        } catch (IllegalStateException e) {}
    }

    @Test
    public void onSuccess_startBeforeAttach() {
        try {
            taskManager.start(id, single);
            fail("Expected IllegalStateException when starting a task with not attached ID");
        } catch (IllegalStateException e) {}
    }

    @Test
    public void onSuccess_startAfterDetach() {
        taskManager.attachSingle(id, onSuccess);
        taskManager.detach(id);
        try {
            taskManager.start(id, single);
            fail("Expected IllegalStateException when starting a task with detached ID");
        } catch (IllegalStateException e) {}
    }

    @Test
    public void onSuccess_startAfterAlreadyStarted() throws Exception {
        taskManager.attachSingle(id, onSuccess);
        taskManager.start(id, single);
        verify(onSuccess, never()).accept(anyString());

        // if old single is not completed, just ignore new single
        Subject<String> subject2 = PublishSubject.create();
        Single<String> single2 = subject2.singleOrError();
        taskManager.start(id, single2);
        assertFalse(subject2.hasObservers());
        onSuccess(subject2, "one");
        verify(onSuccess, never()).accept(anyString());
        onSuccess("two");
        verify(onSuccess).accept("two");
    }

    @Test
    public void onSuccess_startAfterCompleted() throws Exception {
        taskManager.attachSingle(id, onSuccess);
        taskManager.start(id, single);
        verify(onSuccess, never()).accept(anyString());
        onSuccess("one");
        verify(onSuccess).accept("one");

        Subject<String> subject2 = PublishSubject.create();
        Single<String> single2 = subject2.singleOrError();
        taskManager.start(id, single2);
        onSuccess(subject2, "two");
        verify(onSuccess).accept("two");
    }

    private void onSuccess(String value) {
        onSuccess(subject, value);
    }

    private void onSuccess(Subject<String> subject, String value) {
        subject.onNext(value);
        subject.onComplete();
    }
}
