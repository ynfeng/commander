package com.github.ynfeng.commander.server.testutil;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import com.google.common.collect.Lists;
import java.util.List;

public class TestableLoggerAppender<E> extends OutputStreamAppender<E> {
    private static List<LoggingEvent> events = Lists.newArrayList();

    @Override
    public void doAppend(E event) {
        super.doAppend(event);
        events.add((LoggingEvent) event);
    }

    public static List<LoggingEvent> getEvents() {
        return events;
    }

    public static void reset() {
        events.clear();
    }
}
