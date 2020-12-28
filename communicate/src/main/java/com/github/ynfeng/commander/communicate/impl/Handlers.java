package com.github.ynfeng.commander.communicate.impl;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.BiConsumer;

public class Handlers {
    private final Map<String, BiConsumer<ServerConnection, ProtocolRequestMessage>> handlers = Maps.newConcurrentMap();

    public void add(String type, BiConsumer<ServerConnection, ProtocolRequestMessage> handler) {
        handlers.putIfAbsent(type, handler);
    }

    public BiConsumer<ServerConnection, ProtocolRequestMessage> get(String type) {
        return handlers.get(type);
    }

    public void remove(String type) {
        handlers.remove(type);
    }
}
