package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.cluster.communicate.protocol.ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class AbstractHandshakeHanderAdapter extends ChannelInboundHandlerAdapter {
    private final ProtocolVersion protocolVersion;

    public AbstractHandshakeHanderAdapter(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    protected void writeProtocolVersion(ChannelHandlerContext ctx) {
        ByteBuf buffer = ctx.alloc().buffer(1);
        buffer.writeByte(protocolVersion.version());
        ctx.writeAndFlush(buffer);
    }

    protected ProtocolVersion protocolVersion() {
        return protocolVersion;
    }

}
