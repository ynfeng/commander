package com.github.ynfeng.commander.server;

import java.util.Objects;
import java.util.function.Consumer;

public class StepExecuteResult<T> {
    private final T result;
    private final Exception e;

    private StepExecuteResult(T result, Exception e) {
        this.result = result;
        this.e = e;
    }

    public static <T> StepExecuteResult<T> exception(Exception e) {
        return new StepExecuteResult<>(null, e);
    }

    public static <T> StepExecuteResult<T> result(T t) {
        return new StepExecuteResult<>(t, null);
    }

    public StepExecuteResult<T> onException(Consumer<Exception> consumer) {
        if (Objects.nonNull(e)) {
            consumer.accept(e);
        }
        return this;
    }

    public StepExecuteResult<T> onResult(Consumer<T> consumer) {
        if (Objects.nonNull(result)) {
            consumer.accept(result);
        }
        return this;
    }

    public void throwServerExceptionIfNecessary() {
        if (Objects.nonNull(e)) {
            throw new ServerException(e);
        }
    }
}
