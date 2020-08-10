package com.github.ynfeng.commander.server;

import java.util.Objects;
import java.util.function.Consumer;

public class CheckedExecuteResult<T> {
    private final T result;
    private final Exception e;

    private CheckedExecuteResult(T result, Exception e) {
        this.result = result;
        this.e = e;
    }

    public static <T> CheckedExecuteResult<T> exception(Exception e) {
        return new CheckedExecuteResult<>(null, e);
    }

    public static <T> CheckedExecuteResult<T> result(T t) {
        return new CheckedExecuteResult<>(t, null);
    }

    public CheckedExecuteResult<T> onException(Consumer<Exception> consumer) {
        if (Objects.nonNull(e)) {
            consumer.accept(e);
        }
        return this;
    }

    public CheckedExecuteResult<T> onResult(Consumer<T> consumer) {
        if (Objects.nonNull(result)) {
            consumer.accept(result);
        }
        return this;
    }

    public CheckedExecuteResult<T> throwServerExceptionIfNecessary() {
        if (Objects.nonNull(e)) {
            throw new ServerException(e);
        }
        return this;
    }
}
