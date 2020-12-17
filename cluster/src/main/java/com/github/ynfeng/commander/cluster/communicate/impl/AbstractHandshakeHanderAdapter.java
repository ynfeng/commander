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

    protected void writeProtocolVersion(String communicateId,
                                        ChannelHandlerContext ctx,
                                        ProtocolVersion protocolVersion) {
        ByteBuf buffer = ctx.alloc().buffer(5);
        buffer.writeInt(communicateId.hashCode());
        buffer.writeByte(protocolVersion.version());
        ctx.writeAndFlush(buffer);
    }

    protected Optional<ProtocolVersion> readProtocolVersion(ByteBuf byteBuf) {
        return ProtocolVersion.valueOf(byteBuf.readByte());
    }

    protected void acceptProtocolVersion(ChannelHandlerContext ctx, ProtocolVersion protocolVersion) {
        logger.debug("accepted protocol version {} to connection {}", protocolVersion, ctx.channel().remoteAddress());
        ctx.pipeline().remove(this);
        ctx.pipeline().addLast("encoder", protocolVersion.newEncoder());
        ctx.pipeline().addLast("decoder", protocolVersion.newDecoder());
    }

    protected boolean checkCommunicateIdOrCloseContext(ChannelHandlerContext ctx,
                                                       String communicateId,
                                                       ByteBuf byteBuf) {
        if (communicateId.hashCode() != byteBuf.readInt()) {
            ctx.close();
            return false;
        }
        return true;
    }
}
