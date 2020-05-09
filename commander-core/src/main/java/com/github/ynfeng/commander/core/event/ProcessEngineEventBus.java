package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.listener.EventListener;
import com.google.common.collect.Lists;
import java.util.List;

public class ProcessEngineEventBus implements EventBus {
    private final List<EventListener> listeners = Lists.newArrayList();

    @Override
    public void publishEvent(Event event) {
        listeners.stream()
            .filter(listener -> listener.interestedOn(event))
            .forEach(listener -> listener.onEvent(event));
    }

    @Override
    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }
}
