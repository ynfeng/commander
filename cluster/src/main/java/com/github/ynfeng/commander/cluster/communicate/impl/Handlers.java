package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.support.Address;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class Handlers {
    private final Map<String, BiFunction<Address, byte[], CompletableFuture<byte[]>>> handlers
        = Maps.newConcurrentMap();

    public void add(String type, BiFunction<Address, byte[], CompletableFuture<byte[]>> handler) {
        handlers.putIfAbsent(type, handler);
    }

    public BiFunction<Address, byte[], CompletableFuture<byte[]>> get(String type) {
        return handlers.get(type);
    }
}
