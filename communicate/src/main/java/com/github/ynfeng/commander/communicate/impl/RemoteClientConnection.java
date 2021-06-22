package com.github.ynfeng.commander.communicate.impl;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class RemoteClientConnection implements ClientConnection {
    private final Channel channel;
    private final ScheduledExecutorService timeoutExecutor;
    private final Map<Long, Callback> callbacks = Maps.newConcurrentMap();
    private final AtomicBoolean closed = new AtomicBoolean();

    public RemoteClientConnection(Channel channel, ScheduledExecutorService timeoutExecutor) {
        this.channel = channel;
        this.timeoutExecutor = timeoutExecutor;
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
    public CompletableFuture<byte[]> sendAndReceive(ProtocolRequestMessage request, Duration timeout) {
        Callback callback = callbacks.computeIfAbsent(request.messageId(),
            k -> new Callback(request.messageId(), request.subject(), timeout));
        channel.writeAndFlush(request).addListener(f -> {
            if (!f.isSuccess()) {
                callback.completeExceptionally(f.cause());
            }
        });
        return callback.future;
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            channel.close();
        }
    }

    @Override
    public void dispatch(ProtocolResponseMessage response) {
        Callback callback = callbacks.remove(response.messageId());
        if (callback != null) {
            callback.complete(response.payload());
        }
    }

    class Callback {
        private final CompletableFuture<byte[]> future;
        private final long messageId;
        private final String subject;
        private ScheduledFuture<?> timeoutFuture;
        private long timeout;

        Callback(long messageId, String subject, Duration timeout) {
            this.subject = subject;
            if (timeout != null) {
                this.timeout = timeout.toMillis();
                timeoutFuture = timeoutExecutor.schedule(this::timeout, this.timeout, TimeUnit.MILLISECONDS);
            }
            future = new CompletableFuture<>();
            this.messageId = messageId;
        }

        private void timeout() {
            future.completeExceptionally(
                new TimeoutException("Request subject " + subject + " timed out in " + timeout + " milliseconds"));
            callbacks.remove(messageId);
        }

        public void completeExceptionally(Throwable cause) {
            future.completeExceptionally(cause);
            timeoutFuture.cancel(false);
        }

        public void complete(byte[] payload) {
            future.complete(payload);
            if (timeoutFuture != null) {
                timeoutFuture.cancel(false);
            }
        }
    }
}
