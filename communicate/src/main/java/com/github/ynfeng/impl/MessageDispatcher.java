package com.github.ynfeng.commander.cluster.communicate.impl;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageDispatcher<T extends ProtocolMessage> extends ChannelInboundHandlerAdapter {
    private final Connection<T> connection;

    public MessageDispatcher(Connection<T> connection) {
        this.connection = Preconditions.checkNotNull(connection, "Connection can't be null.");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        T protocolMessage = (T) msg;
        connection.dispatch(protocolMessage);
    }
}
