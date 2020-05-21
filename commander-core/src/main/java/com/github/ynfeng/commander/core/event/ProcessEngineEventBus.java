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

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void removeAllListeners() {
        listeners.clear();
    }

    public void removeListener(EventListener eventListener) {
        listeners.remove(eventListener);
    }

    public int numOfListeners() {
        return listeners.size();
    }
}
