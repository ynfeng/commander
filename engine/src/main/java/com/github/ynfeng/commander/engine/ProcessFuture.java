package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.support.CheckedRunnable;
import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ProcessFuture {
    private final Map<String, CompletableFuture<?>> conditions = Maps.newConcurrentMap();
    private final CompletableFuture<ProcessResult> completeFuture = new CompletableFuture<>();
    private final CompletableFuture<ProcessId> processIdFuture = new CompletableFuture<>();

    public ProcessFuture() {
        completeFuture.whenComplete((v, t) -> {
            if (t != null) {
                conditions.forEach((key, value) -> value.completeExceptionally(t));
            }
        });
    }

    private static String getStartNotificationKey(String refName) {
        return String.format("%s##start##", refName);
    }

    private static String getCompleteNotificationKey(String refName) {
        return String.format("%s##complete##", refName);
    }

    private void checkOccurredException(CompletableFuture<?> condition) {
        if (completeFuture.isCompletedExceptionally()) {
            try {
                completeFuture.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                condition.completeExceptionally(e);
                conditions.forEach((key, value) -> value.completeExceptionally(e));
            } catch (Exception e) {
                condition.completeExceptionally(e);
                conditions.forEach((key, value) -> value.completeExceptionally(e));
            }
        }
    }

    public ProcessFuture waitNodeComplete(String refName, Duration duration) {
        return doWithException(() -> {
            CompletableFuture<?> condition = conditions
                .computeIfAbsent(getCompleteNotificationKey(refName), k -> new CompletableFuture<>());
            checkOccurredException(condition);
            condition.get(duration.toMillis(), TimeUnit.MILLISECONDS);
        });
    }

    public ProcessId processId(Duration waitTime) {
        try {
            checkOccurredException(processIdFuture);
            return processIdFuture.get(waitTime.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ProcessFutureException(e);
        } catch (Exception e) {
            throw new ProcessFutureException(e);
        }
    }

    public ProcessFuture waitNodeStart(String refName, Duration duration) {
        return doWithException(() -> {
            CompletableFuture<?> condition = conditions
                .computeIfAbsent(getStartNotificationKey(refName), k -> new CompletableFuture<>());
            checkOccurredException(condition);
            condition.get(duration.toMillis(), TimeUnit.MILLISECONDS);
        });
    }

    protected void notifyNodeComplete(String refName) {
        conditions.get(getCompleteNotificationKey(refName))
            .complete(null);
    }

    protected void notifyNodeStart(String refName) {
        conditions.get(getStartNotificationKey(refName))
            .complete(null);
    }

    protected void makeNotifyCondition(String refName) {
        conditions
            .computeIfAbsent(getStartNotificationKey(refName), k -> new CompletableFuture<>());
        conditions
            .computeIfAbsent(getCompleteNotificationKey(refName), k -> new CompletableFuture<>());
    }

    private ProcessFuture doWithException(CheckedRunnable runner) {
        try {
            runner.run();
        } catch (Exception e) {
            throw new ProcessFutureException(e);
        }
        return this;
    }

    public ProcessFuture waitProcessComplete(Duration waitTime) {
        return doWithException(() ->
            completeFuture.get(waitTime.toMillis(), TimeUnit.MILLISECONDS));
    }

    public List<String> executedNodes() {
        try {
            return completeFuture.get().getExecutedNodes();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ProcessFutureException(e);
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

    public void notifyProcessStarted(ProcessId processId) {
        processIdFuture.complete(processId);
    }
}
