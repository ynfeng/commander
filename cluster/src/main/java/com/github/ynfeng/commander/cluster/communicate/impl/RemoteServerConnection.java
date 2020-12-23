package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.support.Address;
import io.netty.channel.Channel;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class ServerConnection extends AbstractConnection {
    private final Handlers handlers;

    public ServerConnection(Channel channel, Handlers handlers) {
        super(channel);
        this.handlers = handlers;
    }

    @Override
    public void dispatch(ProtocolMessage protocolMessage) {
        BiFunction<Address, byte[], CompletableFuture<byte[]>> handler = handlers.get(protocolMessage.subject());
        if (handler != null) {
            handler.apply(protocolMessage.address(), protocolMessage.payload())
                .complete(protocolMessage.payload());
        }
    }
}
