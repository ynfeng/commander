package com.github.ynfeng.commander.communicate.impl;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class RemoteClientConnection implements ClientConnection {
    private final Channel channel;
    private CompletableFuture<byte[]> completableFuture;
    private final AtomicBoolean closed = new AtomicBoolean();

    public RemoteClientConnection(Channel channel) {
        this.channel = channel;
        closed.set(true);
    }

    @Override
    public CompletableFuture<Void> sendAsync(ProtocolRequestMessage request) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        channel.writeAndFlush(request).addListener(completeFuture(completableFuture));
        return completableFuture;
    }

    @SuppressWarnings("checkstyle:LineLength")
    private static GenericFutureListener<Future<? super Void>> completeFuture(CompletableFuture<Void> completableFuture) {
        return f -> {
            if (f.isSuccess()) {
                completableFuture.complete(null);
            } else {
                completableFuture.completeExceptionally(f.cause());
            }
        };
    }

    @Override
    public CompletableFuture<byte[]> sendAndReceive(ProtocolRequestMessage request) {
        completableFuture = new CompletableFuture<>();
        channel.writeAndFlush(request).addListener(f -> {
            if (!f.isSuccess()) {
                completableFuture.completeExceptionally(f.cause());
            }
        });
        return completableFuture;
    }

    @Override
    public void close() {
        if (closed.compareAndSet(true, false)) {
            channel.close();
        }
    }

    @Override
    public void dispatch(ProtocolResponseMessage response) {
        completableFuture.complete(response.payload());
    }
}
