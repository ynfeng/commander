package com.github.ynfeng.commander.cluster.communicate.impl;

import java.util.concurrent.atomic.AtomicLong;

public class MessageIdGenerator {
    private static final AtomicLong ID = new AtomicLong(0);

    private MessageIdGenerator() {
    }

    public static long nextId() {
        return ID.incrementAndGet();
    }
}
