package com.github.ynfeng.commander.core.event;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ProcessEngineEventBus implements EventBus {
    private final ConcurrentLinkedQueue<EventListener> listeners = new ConcurrentLinkedQueue<>();

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

    @Override
    public void removeAllListeners() {
        listeners.clear();
    }

    @Override
    public void removeListener(EventListener eventListener) {
        listeners.remove(eventListener);
    }

    @Override
    public int numOfListeners() {
        return listeners.size();
    }
}
