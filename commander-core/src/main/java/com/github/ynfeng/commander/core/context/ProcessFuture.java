package com.github.ynfeng.commander.core.context;

import com.github.ynfeng.commander.core.engine.ProcessEngineException;
import java.util.concurrent.Future;

public class ProcessFuture {
    private final Future<?> future;
    private final ProcessId processId;

    private ProcessFuture(ProcessId processId, Future<?> future) {
        this.processId = processId;
        this.future = future;
    }

    public static ProcessFuture create(ProcessId processId, Future<?> future) {
        return new ProcessFuture(processId, future);
    }

    public ProcessId processId() {
        return processId;
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
