package com.github.ynfeng.commander.core.engine;

import com.github.ynfeng.commander.core.Variables;
import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.context.event.EngineEventSubject;
import com.github.ynfeng.commander.core.context.event.ProcessContextClearedEvent;
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
        if (isProcessRunFinish()) {
            return this;
        }
        tryToWaitThrowException(timeout, timeUnit);
        return this;
    }

    private boolean isProcessRunFinish() {
        return processContext.status() == ProcessStatus.COMPLETED || processContext.status() == ProcessStatus.FAILED;
    }

    private void tryToWaitThrowException(int timeout, TimeUnit timeUnit) throws InterruptedException {
        tryToWaitNotThrowException(timeout, timeUnit);
        throwProcessEngineExceptionIfNecessary();
    }

    private void tryToWaitNotThrowException(int timeout, TimeUnit timeUnit) throws InterruptedException {
        synchronized (this) {
            if (isProcessRunFinish()) {
                return;
            }
            doWait(timeout, timeUnit);
        }
        EngineEventSubject.getInstance().removeListener(completeEventListener);
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

    public ProcessFuture syncNotThrowException() throws InterruptedException {
        return syncNotThrowException(Integer.MAX_VALUE, TimeUnit.DAYS);
    }

    public ProcessFuture syncNotThrowException(int timeout, TimeUnit timeUnit) throws InterruptedException {
        if (isProcessRunFinish()) {
            return this;
        }
        tryToWaitNotThrowException(timeout, timeUnit);
        return this;
    }

    public Variables contextVariables() {
        return processContext.input();
    }

    @SuppressWarnings("UnstableApiUsage")
    class ProcessCompleteEventListener {

        @Subscribe
        public void handleEvent(ProcessContextClearedEvent event) {
            ProcessId processsId = event.processContext().processId();
            if (processsId.equals(processId())) {
                synchronized (ProcessFuture.this) {
                    ProcessFuture.this.notifyAll();
                }
            }
        }
    }

}
