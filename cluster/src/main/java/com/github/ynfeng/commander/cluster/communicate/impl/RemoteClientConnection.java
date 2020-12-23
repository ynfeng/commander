package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.channel.Channel;

public class RemoteClientConnection extends AbstractConnection {

    public RemoteClientConnection(Channel remoteChannel) {
        super(remoteChannel);
    }

    @Override
    public void dispatch(ProtocolMessage protocolMessage) {

    }
}
