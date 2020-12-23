package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.channel.Channel;

public class ServerConnection extends AbstractConnection {
    private final Handlers handlers;

    public ServerConnection(Channel channel, Handlers handlers) {
        super(channel);
        this.handlers = handlers;
    }

    @Override
    public void dispatch(ProtocolMessage protocolMessage) {
        handlers.get(protocolMessage.subject())
            .apply(protocolMessage.address(), protocolMessage.payload())
            .complete(protocolMessage.payload());
    }
}
