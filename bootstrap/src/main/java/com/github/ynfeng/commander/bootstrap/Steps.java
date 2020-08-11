package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.support.CheckedCallable;
import com.github.ynfeng.commander.support.CheckedExecuteResult;
import com.github.ynfeng.commander.support.CheckedRunnable;
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
