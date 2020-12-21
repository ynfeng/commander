package com.github.ynfeng.commander.cluster.communicate.impl;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageDispatcher extends ChannelInboundHandlerAdapter {
    private final Connection connection;

    public MessageDispatcher(Connection connection) {
        this.connection = Preconditions.checkNotNull(connection, "Connection can't be null.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolMessage protocolMessage = (ProtocolMessage) msg;
        connection.dispatch(protocolMessage);
    }
}
