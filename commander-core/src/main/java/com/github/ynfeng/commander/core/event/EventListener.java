package com.github.ynfeng.commander.core.event;

public interface EventListener {
    void onEvent(Event event);

    boolean interestedOn(Event event);
}
