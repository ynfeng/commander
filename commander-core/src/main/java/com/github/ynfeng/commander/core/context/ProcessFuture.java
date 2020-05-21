package com.github.ynfeng.commander.core.context;

import com.github.ynfeng.commander.core.engine.ProcessEngineException;
import java.util.concurrent.Future;

public class ProcessFuture {
    private final Future<?> future;
    private final ProcessContext processContext;

    private ProcessFuture(ProcessContext processContext, Future<?> future) {
        this.processContext = processContext;
        this.future = future;
    }

    public static ProcessFuture create(ProcessContext processContext, Future<?> future) {
        return new ProcessFuture(processContext, future);
    }

    public ProcessId processId() {
        return processContext.processId();
    }

    public ProcessFuture waitComplete() {
        try {
            future.get();
            return this;
        } catch (Throwable e) {
            Throwable cause = e.getCause();
            if (cause instanceof ProcessEngineException) {
                throw (ProcessEngineException) cause;
            } else {
                throw new ProcessEngineException(cause);
            }
        }
    }
}
