package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

public class RemoteClientConnection implements ClientConnection {
    private final Channel channel;
    private CompletableFuture<byte[]> completableFuture;

    public RemoteClientConnection(Channel channel) {
        this.channel = channel;
    }

    @Override
    public CompletableFuture<Void> sendAsync(ProtocolMessage message) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        channel.writeAndFlush(message).addListener(completeFuture(completableFuture));
        return completableFuture;
    }

    @NotNull
    private GenericFutureListener<Future<? super Void>> completeFuture(CompletableFuture<Void> completableFuture) {
        return f -> {
            if (f.isSuccess()) {
                completableFuture.complete(null);
            } else {
                completableFuture.completeExceptionally(f.cause());
            }
        };
    }

    @Override
    public CompletableFuture<byte[]> sendAndReceive(ProtocolMessage protocolMessage) {
        completableFuture = new CompletableFuture<>();
        channel.writeAndFlush(protocolMessage).addListener(f -> {
            if (!f.isSuccess()) {
                completableFuture.completeExceptionally(f.cause());
            }
        });
        return completableFuture;
    }

    @Override
    public void dispatch(ProtocolMessage protocolMessage) {
        completableFuture.complete(protocolMessage.payload());
    }
}
