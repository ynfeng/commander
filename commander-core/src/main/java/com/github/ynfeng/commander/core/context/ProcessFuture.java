package com.github.ynfeng.commander.core.context;

import com.github.ynfeng.commander.core.engine.ProcessEngineException;
import com.github.ynfeng.commander.core.event.Event;
import com.github.ynfeng.commander.core.event.EventListener;
import com.github.ynfeng.commander.core.event.ProcessExecuteCompleteEvent;
import java.util.concurrent.TimeUnit;

public class ProcessFuture {
    private final ProcessContext processContext;
    private final Object monitor = new Object();
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

    @SuppressWarnings("WaitNotInLoop")
    public ProcessFuture waitComplete(int timeout, TimeUnit timeUnit) throws InterruptedException {
        if (processContext.status() != ProcessStatus.COMPLETED) {
            registerProcessCompleteEventListener();
            if (processContext.status() == ProcessStatus.COMPLETED) {
                EngineContext.removeEventListener(eventListener);
                return this;
            }
            synchronized (monitor) {
                monitor.wait(timeUnit.toMillis(timeout));
            }
            EngineContext.removeEventListener(eventListener);
            throwProcessEngineExceptionIfNecessary();
        }
        return this;
    }

    private ProcessCompleteEventListener registerProcessCompleteEventListener() {
        eventListener = new ProcessCompleteEventListener();
        EngineContext.registerEventListener(eventListener);
        return eventListener;
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
            ProcessExecuteCompleteEvent processExecuteCompleteEvent = (ProcessExecuteCompleteEvent) event;
            ProcessId processsId = processExecuteCompleteEvent.context().processId();
            if (processsId.equals(processId())) {
                synchronized (monitor) {
                    monitor.notifyAll();
                }
            }
        }

        @Override
        public boolean interestedOn(Event event) {
            return event instanceof ProcessExecuteCompleteEvent;
        }
    }

}
