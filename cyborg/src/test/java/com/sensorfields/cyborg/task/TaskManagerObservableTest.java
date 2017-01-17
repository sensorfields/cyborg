package com.sensorfields.cyborg.task;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class TaskManagerObservableTest {

    private TaskManager taskManager;
    private String id;
    private Subject<String> subject;
    private Consumer<String> onNext;

    @Before
    public void setup() {
        taskManager = new TaskManager();
        id = UUID.randomUUID().toString();
        subject = PublishSubject.create();
        onNext = mock(Consumer.class);
    }

    @Test
    public void onNext() throws Exception {
        // we get the value
        taskManager.attachObservable(id, onNext);
        taskManager.start(id, subject);
        verify(onNext, never()).accept(anyString());
        subject.onNext("one");
        verify(onNext).accept("one");

        // we get new value if starting again
        subject.onComplete();
        subject = PublishSubject.create();
        taskManager.start(id, subject);
        subject.onNext("two");
        verify(onNext).accept("two");

        // we do not get the value again if we detach and attach
        onNext = mock(Consumer.class);
        taskManager.detach(id);
        taskManager.attachObservable(id, onNext);
        verify(onNext, never()).accept(anyString());

        // we don't get the value if we are detached, but get the value after attach
        subject.onComplete();
        subject = PublishSubject.create();
        taskManager.start(id, subject);
        taskManager.detach(id);
        subject.onNext("three");
        verify(onNext, never()).accept(anyString());
        taskManager.attachObservable(id, onNext);
        verify(onNext).accept("three");
    }
}
