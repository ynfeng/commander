package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.listener.EventListener;

public interface EventBus {
    void publishEvent(Event event);

    void registerListener(EventListener eventListener);
}
