package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.channel.Channel;
import java.util.concurrent.CompletableFuture;

public class AbstractConnection implements Connection {
    private final Channel channel;
    private final Handlers handlers;

    public AbstractConnection(Channel channel, Handlers handlers) {
        this.channel = channel;
        this.handlers = handlers;
    }

    @Override
    public CompletableFuture<Void> sendAsync(ProtocolMessage message) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        channel.writeAndFlush(message).syncUninterruptibly();
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public void dispatch(ProtocolMessage protocolMessage) {
        handlers.get(protocolMessage.subject())
            .apply(protocolMessage.address(), protocolMessage.payload())
            .complete(protocolMessage.payload());
    }
}
