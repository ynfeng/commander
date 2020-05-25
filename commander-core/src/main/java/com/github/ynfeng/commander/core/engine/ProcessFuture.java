package com.github.ynfeng.commander.core.engine;

import com.github.ynfeng.commander.core.context.EngineEventSubject;
import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.event.Event;
import com.github.ynfeng.commander.core.event.EventListener;
import com.github.ynfeng.commander.core.event.ProcessExecuteCompletedEvent;
import com.github.ynfeng.commander.core.exception.ProcessEngineException;
import java.util.concurrent.TimeUnit;

public class ProcessFuture {
    private final ProcessContext processContext;
    private ProcessCompleteEventListener eventListener;

    private ProcessFuture(ProcessContext processContext) {
        this.processContext = processContext;
    }

    public static ProcessFuture create(ProcessContext processContext) {
        return new ProcessFuture(processContext);
    }

    public ProcessId processId() {
        return processContext.processId();
    }

    public ProcessFuture waitComplete() throws InterruptedException {
        return waitComplete(365, TimeUnit.DAYS);
    }

    public ProcessFuture waitComplete(int timeout, TimeUnit timeUnit) throws InterruptedException {
        if (processContext.status() == ProcessStatus.COMPLETED) {
            return this;
        }
        tryToWait(timeout, timeUnit);
        return this;
    }

    private void tryToWait(int timeout, TimeUnit timeUnit) throws InterruptedException {
        synchronized (this) {
            if (processContext.status() == ProcessStatus.COMPLETED) {
                return;
            }
            doWait(timeout, timeUnit);
        }
        EngineEventSubject.getInstance().removeListener(eventListener);
        throwProcessEngineExceptionIfNecessary();
    }

    @SuppressWarnings("WaitNotInLoop")
    private void doWait(int timeout, TimeUnit timeUnit) throws InterruptedException {
        registerProcessCompleteEventListener();
        wait(timeUnit.toMillis(timeout));
    }

    private void registerProcessCompleteEventListener() {
        eventListener = new ProcessCompleteEventListener();
        EngineEventSubject.getInstance().registerListener(eventListener);
    }

    private void throwProcessEngineExceptionIfNecessary() {
        if (processContext.hasException()) {
            Throwable throwable = processContext.executeException();
            if (throwable instanceof ProcessEngineException) {
                throw (ProcessEngineException) throwable;
            }
            throw new ProcessEngineException(throwable);
        }
    }

    class ProcessCompleteEventListener implements EventListener {
        @Override
        public void onEvent(Event event) {
            ProcessId processsId = ProcessContext.get().processId();
            if (processsId.equals(processId())) {
                synchronized (ProcessFuture.this) {
                    ProcessFuture.this.notifyAll();
                }
            }
        }

        @Override
        public boolean interestedOn(Event event) {
            return event instanceof ProcessExecuteCompletedEvent;
        }
    }

}
