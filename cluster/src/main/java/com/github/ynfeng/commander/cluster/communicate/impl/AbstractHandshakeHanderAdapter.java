package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.cluster.communicate.protocol.ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class AbstractHandshakeHanderAdapter extends ChannelInboundHandlerAdapter {

    protected void writeProtocolVersion(ChannelHandlerContext ctx, ProtocolVersion protocolVersion) {
        ByteBuf buffer = ctx.alloc().buffer(1);
        buffer.writeByte(protocolVersion.version());
        ctx.writeAndFlush(buffer);
    }
}
