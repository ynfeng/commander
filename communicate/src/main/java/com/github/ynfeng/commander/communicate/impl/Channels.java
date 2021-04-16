package com.github.ynfeng.commander.communicate.impl;

import com.github.ynfeng.commander.support.Address;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Channels {
    private final Map<Address, Channel> channelMap = Maps.newConcurrentMap();

    public CompletableFuture<Channel> get(Address address, Supplier<CompletableFuture<Channel>> supplier) {
        Channel channel = channelMap.get(address);
        if (channel == null) {
            channel = supplier.get().join();
            channel.closeFuture().addListener(ch -> channelMap.remove(address));
            channelMap.putIfAbsent(address, channel);
        }
        return CompletableFuture.completedFuture(channel);
    }

    public void remove(Address address) {
        channelMap.remove(address);
    }
}
