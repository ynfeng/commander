package com.github.ynfeng.commander.engine;

import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ProcessFuture {
    private final Map<String, CompletableFuture<?>> conditions = Maps.newConcurrentMap();
    private final CompletableFuture<ProcessResult> completeFuture = new CompletableFuture<>();

    public ProcessFuture() {
        completeFuture.whenComplete((r, t) -> {
            conditions.forEach((key, value) -> value.completeExceptionally(t));
        });
    }

    public ProcessFuture waitNodeComplete(String refName, Duration duration) {
        return doWithException(() -> {
            CompletableFuture<?> condition = conditions
                .computeIfAbsent(refName, k -> new CompletableFuture<>());
            checkOccurredException(condition);
            condition.get(duration.toMillis(), TimeUnit.MILLISECONDS);
        });
    }

    private void checkOccurredException(CompletableFuture<?> condition) {
        if (completeFuture.isCompletedExceptionally()) {
            try {
                completeFuture.get();
            } catch (Exception e) {
                condition.completeExceptionally(e);
            }
        }
    }

    public ProcessFuture waitNodeStart(String refName, Duration duration) {
        return doWithException(() -> {
            CompletableFuture<?> condition = conditions
                .computeIfAbsent(getStartNodeKey(refName), k -> new CompletableFuture<>());
            checkOccurredException(condition);
            condition.get(duration.toMillis(), TimeUnit.MILLISECONDS);
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

    public ProcessFuture waitProcessComplete(Duration waitTime) {
        return doWithException(() -> {
            completeFuture.get(waitTime.toMillis(), TimeUnit.MILLISECONDS);
        });
    }

    public List<String> executedNodes() {
        try {
            return completeFuture.get().getExecutedNodes();
        } catch (Exception e) {
            throw new ProcessFutureException(e);
        }
    }

    public void notifyProcessComplete(ProcessResult result) {
        completeFuture.complete(result);
    }

    public void notifyProcessCompleteExceptionally(Throwable t) {
        completeFuture.completeExceptionally(t);
    }
}
