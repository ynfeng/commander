package com.github.ynfeng.commander.cluster.communicate.impl;

import java.util.concurrent.CompletableFuture;

public interface ClientConnection extends Connection<ProtocolResponseMessage> {
    CompletableFuture<Void> sendAsync(ProtocolRequestMessage request);

    CompletableFuture<byte[]> sendAndReceive(ProtocolRequestMessage request);
}
