package com.github.ynfeng.commander.cluster.communicate.impl;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.BiConsumer;

public class Handlers {
    private final Map<String, BiConsumer<ServerConnection, ProtocolMessage>> handlers = Maps.newConcurrentMap();

    public void add(String type, BiConsumer<ServerConnection, ProtocolMessage> handler) {
        handlers.putIfAbsent(type, handler);
    }

    public BiConsumer<ServerConnection, ProtocolMessage> get(String type) {
        return handlers.get(type);
    }

    public void remove(String type) {
        handlers.remove(type);
    }
}
