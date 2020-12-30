package com.github.ynfeng.commander.communicate.impl;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class RemoteClientConnection implements ClientConnection {
    private final Channel channel;
    private final Map<Long, CompletableFuture<byte[]>> waitingReceiveFuture = Maps.newConcurrentMap();
    private final AtomicBoolean closed = new AtomicBoolean();

    public RemoteClientConnection(Channel channel) {
        this.channel = channel;
        closed.set(false);
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
        CompletableFuture<byte[]> completableFuture
            = waitingReceiveFuture.computeIfAbsent(request.messageId(), k -> new CompletableFuture<>());
        channel.writeAndFlush(request).addListener(f -> {
            if (!f.isSuccess()) {
                completableFuture.completeExceptionally(f.cause());
            }
        });
        return completableFuture;
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            channel.close();
        }
    }

    @Override
    public void dispatch(ProtocolResponseMessage response) {
        CompletableFuture<byte[]> future = waitingReceiveFuture.remove(response.messageId());
        if (future != null) {
            future.complete(response.payload());
        }
    }
}
