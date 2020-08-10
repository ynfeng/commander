package com.github.ynfeng.commander.server;

import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;

public class Steps {
    protected long takeDuration(CheckedRunnable runnable) throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        runnable.run();
        stopwatch.stop();
        return stopwatch.elapsed(TimeUnit.MILLISECONDS);
    }

    protected <T> CheckedExecuteResult<T> executeChecked(CheckedCallable<T> callable) {
        try {
            T t = callable.call();
            return CheckedExecuteResult.result(t);
        } catch (Exception e) {
            return CheckedExecuteResult.exception(e);
        }
    }
}
