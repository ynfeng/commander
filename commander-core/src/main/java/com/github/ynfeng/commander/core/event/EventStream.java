package com.github.ynfeng.commander.core.event;

import com.google.common.eventbus.EventBus;

public final class EventStream {
    private static final EventStream INSTANCE = new EventStream();
    private EventBus eventBus = new EventBus();

    public EventStream() {
    }

    public static EventStream getInstance() {
        return INSTANCE;
    }

    public void subcribe(Object listener) {
        eventBus.register(listener);
    }

    public void unsubcribe(Object listener) {
        eventBus.unregister(listener);
    }

    public void publish(Event event) {
        eventBus.post(event);
    }

    public void xxxx() {
        eventBus = new EventBus();
    }
}
