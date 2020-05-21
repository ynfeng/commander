package com.github.ynfeng.commander.core.context;

import com.github.ynfeng.commander.core.engine.ProcessEngineException;
import java.util.concurrent.Future;

public class ProcessStartFuture {
    private final Future<?> future;
    private final ProcessId processId;

    private ProcessStartFuture(ProcessId processId, Future<?> future) {
        this.processId = processId;
        this.future = future;
    }

    public static ProcessStartFuture create(ProcessId processId, Future<?> future) {
        return new ProcessStartFuture(processId, future);
    }

    public ProcessId processId() {
        return processId;
    }

    public ProcessStartFuture waitComplete() {
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
