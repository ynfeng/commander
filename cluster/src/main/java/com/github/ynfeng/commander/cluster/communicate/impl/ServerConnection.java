package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.channel.Channel;

public class ServerConnection extends AbstractConnection {

    public ServerConnection(Channel channel, Handlers handlers) {
        super(channel, handlers);
    }
}
