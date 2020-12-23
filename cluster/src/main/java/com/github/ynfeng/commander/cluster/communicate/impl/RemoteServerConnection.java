package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.channel.Channel;
import java.util.function.BiConsumer;

public class RemoteServerConnection implements ServerConnection {
    private final Channel channel;
    private final Handlers handlers;

    public RemoteServerConnection(Channel channel, Handlers handlers) {
        this.channel = channel;
        this.handlers = handlers;
    }

    @Override
    public void dispatch(ProtocolMessage protocolMessage) {
        BiConsumer<ServerConnection, ProtocolMessage> handler = handlers.get(protocolMessage.subject());
        if (handler != null) {
            handler.accept(this, protocolMessage);
        }
    }

    @Override
    public void reply(ProtocolMessage message) {

    }
}
