package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;

public class AbstractConnection implements Connection {
    private final Channel channel;
    private final Handlers handlers;

    public AbstractConnection(Channel channel, Handlers handlers) {
        this.channel = channel;
        this.handlers = handlers;
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

    @Override
    public void dispatch(ProtocolMessage protocolMessage) {
        handlers.get(protocolMessage.subject())
            .apply(protocolMessage.address(), protocolMessage.payload())
            .complete(protocolMessage.payload());
    }
}
