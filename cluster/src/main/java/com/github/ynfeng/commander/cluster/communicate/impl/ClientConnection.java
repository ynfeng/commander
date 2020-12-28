package com.github.ynfeng.commander.cluster.communicate.impl;

import java.util.concurrent.CompletableFuture;

public interface ClientConnection extends Connection {
    CompletableFuture<Void> sendAsync(ProtocolMessage message);

    CompletableFuture<byte[]> sendAndReceive(ProtocolMessage protocolMessage);
}
