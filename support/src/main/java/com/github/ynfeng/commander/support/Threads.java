package com.github.ynfeng.commander.support;

import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ThreadFactory;

public class Threads {
    private Threads() {
    }

    public static ThreadFactory namedThreads(String patten, CmderLogger logger) {
        return new ThreadFactoryBuilder()
            .setNameFormat(patten)
            .setThreadFactory(Thread::new)
            .setUncaughtExceptionHandler((t, e) -> logger.error("Uncaught exception on " + t.getName(), e))
            .build();
    }
}
