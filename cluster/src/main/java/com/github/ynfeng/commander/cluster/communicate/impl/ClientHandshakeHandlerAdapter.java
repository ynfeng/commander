package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.cluster.communicate.protocol.ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandshakeHandlerAdapter extends AbstractHandshakeHanderAdapter {
    private final String communicateId;
    private final ProtocolVersion protocolVersion;

    public ClientHandshakeHandlerAdapter(String communicateId, ProtocolVersion protocolVersion) {
        this.communicateId = communicateId;
        this.protocolVersion = protocolVersion;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        writeProtocolVersion(communicateId, ctx, protocolVersion);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        if (checkCommunicateIdOrCloseContext(ctx, communicateId, byteBuf)) {
            readProtocolVersion(byteBuf)
                .ifPresent(version -> acceptProtocolVersion(ctx, version));
        }
    }

}
