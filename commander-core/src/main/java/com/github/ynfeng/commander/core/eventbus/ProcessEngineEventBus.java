package com.github.ynfeng.commander.core.eventbus;

import com.github.ynfeng.commander.core.event.Event;
import com.github.ynfeng.commander.core.event.EventListener;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProcessEngineEventBus implements EventBus {
    public static final ProcessEngineEventBus INSTANCE = new ProcessEngineEventBus();
    private final ConcurrentLinkedQueue<EventListener> listeners = new ConcurrentLinkedQueue<>();

    public static ProcessEngineEventBus getInstance() {
        return INSTANCE;
    }

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
