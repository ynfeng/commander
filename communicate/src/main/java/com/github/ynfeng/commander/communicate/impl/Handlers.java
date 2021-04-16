package com.github.ynfeng.commander.communicate.impl;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.BiConsumer;

public class Handlers {
    private final Map<String, BiConsumer<ServerConnection, ProtocolRequestMessage>>
        handlerMap = Maps.newConcurrentMap();

    public void add(String type, BiConsumer<ServerConnection, ProtocolRequestMessage> handler) {
        handlerMap.putIfAbsent(type, handler);
    }

    public BiConsumer<ServerConnection, ProtocolRequestMessage> get(String type) {
        return handlerMap.get(type);
    }

    public void remove(String type) {
        handlerMap.remove(type);
    }
}
