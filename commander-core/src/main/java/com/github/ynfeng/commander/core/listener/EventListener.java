package com.github.ynfeng.commander.core.listener;

import com.github.ynfeng.commander.core.event.Event;

public interface EventListener {
    void onEvent(Event event);

    boolean interestedOn(Event event);
}
