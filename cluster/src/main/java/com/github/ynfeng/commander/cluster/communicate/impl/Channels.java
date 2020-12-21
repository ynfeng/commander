package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.support.Address;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Channels {
    private final Map<Address, Channel> channels = Maps.newConcurrentMap();

    public CompletableFuture<Channel> get(Address address, Supplier<CompletableFuture<Channel>> supplier) {
        Channel channel = channels.get(address);
        if (channel == null) {
            return supplier.get().thenApply(ch -> {
                channels.putIfAbsent(address, ch);
                return ch;
            });
        }
        return CompletableFuture.completedFuture(channel);
    }

    public void add(Address address, Channel channel) {
        channels.putIfAbsent(address, channel);
    }
}
