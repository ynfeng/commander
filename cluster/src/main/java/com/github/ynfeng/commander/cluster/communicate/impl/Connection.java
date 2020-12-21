package com.github.ynfeng.commander.cluster.communicate.impl;

import java.util.concurrent.CompletableFuture;

public interface Connection {
    CompletableFuture<Void> sendAsync(ProtocolMessage message);

    void dispatch(ProtocolMessage protocolMessage);
}
