package com.github.ynfeng.commander.communicate.impl;

import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Optional;
import org.slf4j.Logger;

public abstract class AbstractHandshakeHanderAdapter extends ChannelInboundHandlerAdapter {
    private final Logger logger = CmderLoggerFactory.getSystemLogger();

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

    @SuppressWarnings( {"unchecked", "rawtypes"})
    protected void acceptProtocolVersion(ChannelHandlerContext ctx,
                                         Connection<? extends ProtocolMessage> connection,
                                         ProtocolVersion protocolVersion) {
        logger.debug("accepted protocol version {} to connection {}", protocolVersion, ctx.channel().remoteAddress());
        ctx.pipeline().remove(this);
        ctx.pipeline().remove(NettyMessagingService.HANDSHAKE_FRAME_DECODER);
        ctx.pipeline().addLast("encoder", protocolVersion.newEncoder());
        ctx.pipeline().addLast(NettyMessagingService.HANDSHAKE_FRAME_DECODER, protocolVersion.newFrameDecoder());
        ctx.pipeline().addLast("decoder", protocolVersion.newDecoder());
        ctx.pipeline().addLast("dispatcher", new MessageDispatcher(connection));
    }

    protected boolean checkCommunicateIdOrCloseContext(ChannelHandlerContext ctx,
                                                       String communicateId,
                                                       ByteBuf byteBuf) {
        int peerId = byteBuf.readInt();
        if (communicateId.hashCode() != peerId) {
            ctx.close();
            return false;
        }
        return true;
    }
}
