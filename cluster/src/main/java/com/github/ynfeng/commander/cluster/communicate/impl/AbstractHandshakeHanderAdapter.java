package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.cluster.communicate.protocol.ProtocolVersion;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Optional;

public abstract class AbstractHandshakeHanderAdapter extends ChannelInboundHandlerAdapter {
    private final CmderLogger logger = CmderLoggerFactory.getSystemLogger();

    protected void writeProtocolVersion(ChannelHandlerContext ctx, ProtocolVersion protocolVersion) {
        ByteBuf buffer = ctx.alloc().buffer(1);
        buffer.writeByte(protocolVersion.version());
        ctx.writeAndFlush(buffer);
    }

    protected Optional<ProtocolVersion> readProtocolVersion(ByteBuf byteBuf) {
        try {
            byte version = byteBuf.readByte();
            return ProtocolVersion.valueOf(version);
        } finally {
            byteBuf.release();
        }
    }

    protected void acceptProtocolVersion(ChannelHandlerContext ctx, ProtocolVersion protocolVersion) {
        logger.debug("accepted protocol version {} to connection {}", protocolVersion, ctx.channel().remoteAddress());
        ctx.pipeline().remove(this);
        ctx.pipeline().addLast("encoder", protocolVersion.newEncoder());
        ctx.pipeline().addLast("decoder", protocolVersion.newDecoder());
    }
}
