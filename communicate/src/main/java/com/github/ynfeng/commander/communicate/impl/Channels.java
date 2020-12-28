package com.github.ynfeng.commander.communicate.impl;

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
            channel = supplier.get().join();
            channel.closeFuture().addListener(ch -> channels.remove(address));
            channels.putIfAbsent(address, channel);
        }
        return CompletableFuture.completedFuture(channel);
    }

    public void remove(Address address) {
        channels.remove(address);
    }
}
