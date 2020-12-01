package com.github.ynfeng.commander.engine;

import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ProcessFuture extends CompletableFuture<ProcessInstanceResult> {
    private final Map<String, CompletableFuture<?>> conditions = Maps.newConcurrentMap();

    public ProcessFuture waitNodeComplete(String refName, Duration duration) {
        return doWithException(() -> {
            conditions
                .computeIfAbsent(refName, k -> new CompletableFuture<>())
                .get(duration.toMillis(), TimeUnit.MILLISECONDS);
        });
    }

    public ProcessFuture waitNodeStart(String refName, Duration duration) {
        return doWithException(() -> {
            conditions
                .computeIfAbsent(getStartNodeKey(refName), k -> new CompletableFuture<>())
                .get(duration.toMillis(), TimeUnit.MILLISECONDS);
        });
    }

    protected void notifyNodeComplete(String refName) {
        conditions.get(refName)
            .complete(null);
    }

    protected void notifyNodeStart(String refName) {
        conditions.get(getStartNodeKey(refName))
            .complete(null);
    }

    private String getStartNodeKey(String refName) {
        return String.format("%s##start##", refName);
    }

    protected void makeNotifyCondition(String refName) {
        conditions
            .computeIfAbsent(getStartNodeKey(refName), k -> new CompletableFuture<>());
        conditions
            .computeIfAbsent(refName, k -> new CompletableFuture<>());
    }

    private ProcessFuture doWithException(UnsafeRunner runner) {
        try {
            runner.run();
        } catch (Throwable e) {
            throw new ProcessFutureException(e);
        }
        return this;
    }
}
