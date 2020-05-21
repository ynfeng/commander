package com.github.ynfeng.commander.core.event;

public interface EventBus {
    void publishEvent(Event event);

    void registerListener(EventListener eventListener);

}
