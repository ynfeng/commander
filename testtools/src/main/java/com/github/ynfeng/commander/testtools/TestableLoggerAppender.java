package com.github.ynfeng.commander.testtools;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import com.google.common.collect.Lists;
import java.util.List;

public class TestableLoggerAppender<E> extends OutputStreamAppender<E> {
    private static List<LoggingEvent> events = Lists.newArrayList();

    public static List<LoggingEvent> getEvents() {
        return events;
    }

    public static void reset() {
        events.clear();
    }

    @Override
    public void doAppend(E event) {
        super.doAppend(event);
        events.add((LoggingEvent) event);
    }
}
