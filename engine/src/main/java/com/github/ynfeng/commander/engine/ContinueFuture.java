package com.github.ynfeng.commander.engine;

import com.google.common.base.Stopwatch;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ContinueFuture {
    private final CompletableFuture<ProcessFuture> future = new CompletableFuture<>();

    public void complete(ProcessFuture processFuture) {
        future.complete(processFuture);
    }

    public void completeExceptionally(Throwable e) {
        future.completeExceptionally(e);
    }

    public ProcessFuture waitNodeComplete(String refName, Duration waitTime) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ProcessFuture processFuture = getProcessFuture(waitTime);
        long elapsedTime = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        return processFuture.waitNodeComplete(refName, waitTime.plusMillis(-elapsedTime));
    }

    public ProcessFuture waitProcessComplete(Duration waitTime) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ProcessFuture processFuture = getProcessFuture(waitTime);
        long elapsedTime = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        return processFuture.waitProcessComplete(waitTime.plusMillis(-elapsedTime));
    }

    private ProcessFuture getProcessFuture(Duration duration) {
        try {
            return future.get(duration.toMillis(), TimeUnit.MILLISECONDS);
        } catch (Throwable t) {
            throw new ProcessFutureException(t);
        }
    }
}
