package com.github.ynfeng.commander.core.eventbus;

import com.github.ynfeng.commander.core.event.Event;
import com.github.ynfeng.commander.core.event.EventListener;

public interface EventBus {
    void publishEvent(Event event);

    void registerListener(EventListener eventListener);

}
