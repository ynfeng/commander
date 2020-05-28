package com.github.ynfeng.commander.core.engine;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.context.event.EngineEventSubject;
import com.github.ynfeng.commander.core.context.event.ProcessExecuteCompletedEvent;
import com.github.ynfeng.commander.core.exception.ProcessEngineException;
import com.google.common.eventbus.Subscribe;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessFuture {
    private final ProcessContext processContext;
    private ProcessCompleteEventListener completeEventListener;

    private ProcessFuture(ProcessContext processContext) {
        this.processContext = processContext;
    }

    public static ProcessFuture create(ProcessContext processContext) {
        return new ProcessFuture(processContext);
    }

    public ProcessId processId() {
        return processContext.processId();
    }

    public ProcessFuture sync() throws InterruptedException {
        return sync(Integer.MAX_VALUE, TimeUnit.DAYS);
    }

    public ProcessFuture sync(int timeout, TimeUnit timeUnit) throws InterruptedException {
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
        EngineEventSubject.getInstance().removeListener(completeEventListener);
        throwProcessEngineExceptionIfNecessary();
    }

    @SuppressWarnings("WaitNotInLoop")
    private void doWait(int timeout, TimeUnit timeUnit) throws InterruptedException {
        registerProcessCompleteEventListener();
        wait(timeUnit.toMillis(timeout));
    }

    private void registerProcessCompleteEventListener() {
        completeEventListener = new ProcessCompleteEventListener();
        EngineEventSubject.getInstance().registerListener(completeEventListener);
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

    public List<String> executedNodes() {
        return processContext.executedNodes();
    }

    public ProcessStatus status() {
        return processContext.status();
    }

    class ProcessCompleteEventListener {

        @Subscribe
        public void handleEvent(ProcessExecuteCompletedEvent event) {
            ProcessId processsId = event.processContext().processId();
            if (processsId.equals(processId())) {
                synchronized (ProcessFuture.this) {
                    ProcessFuture.this.notifyAll();
                }
            }
        }
    }

}
