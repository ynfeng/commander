package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.support.Address;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiFunction;

public class Handlers {
    private final Map<String, CopyOnWriteArraySet<BiFunction<Address, byte[], CompletableFuture<byte[]>>>> handlers
        = Maps.newConcurrentMap();

    public void add(String type, BiFunction<Address, byte[], CompletableFuture<byte[]>> handler) {
        handlers.computeIfAbsent(type, k -> Sets.newCopyOnWriteArraySet()).add(handler);
    }

    public Set<BiFunction<Address, byte[], CompletableFuture<byte[]>>> get(String type) {
        return handlers.getOrDefault(type, Sets.newCopyOnWriteArraySet());
    }
}
