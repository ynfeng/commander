package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractConnection implements Connection {
    private final Channel channel;

    public AbstractConnection(Channel channel) {
        this.channel = channel;
    }

    private static void complete(CompletableFuture<Void> completableFuture, Future<? super Void> c) {
        if (c.isSuccess()) {
            completableFuture.complete(null);
        } else {
            completableFuture.completeExceptionally(c.cause());
        }
    }

    @Override
    public CompletableFuture<Void> sendAsync(ProtocolMessage message) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        channel.writeAndFlush(message).addListener(c -> complete(completableFuture, c));
        return completableFuture;
    }
}
