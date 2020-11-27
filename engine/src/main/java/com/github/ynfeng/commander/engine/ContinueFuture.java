package com.github.ynfeng.commander.engine;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ContinueFuture {
    private final CompletableFuture<ProcessFuture> future = new CompletableFuture<>();

    public ProcessFuture getProcessFuture() {
        return future.join();
    }

    public ProcessFuture getProcessFuture(long timeout, TimeUnit timeUnit)  {
        try {
            return future.get(timeout, timeUnit);
        } catch (Throwable t) {
            throw new ProcessFutureException(t);
        }
    }

    public void complete(ProcessFuture processFuture) {
        future.complete(processFuture);
    }

    public void completeExceptionally(Throwable e) {
        future.completeExceptionally(e);
    }
}
