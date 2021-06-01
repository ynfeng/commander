package com.github.ynfeng.commander.support;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ThreadFactory;
import org.slf4j.Logger;

public class Threads {
    private Threads() {
    }

    public static ThreadFactory namedThreads(String patten, Logger logger) {
        return new ThreadFactoryBuilder()
            .setNameFormat(patten)
            .setThreadFactory(Thread::new)
            .setUncaughtExceptionHandler((t, e) -> logger.error("Uncaught exception on " + t.getName(), e))
            .build();
    }
}
