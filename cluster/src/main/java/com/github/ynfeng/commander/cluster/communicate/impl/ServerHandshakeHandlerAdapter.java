package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.cluster.communicate.protocol.ProtocolVersion;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.Optional;

public class ServerHandshakeHandlerAdapter extends AbstractHandshakeHanderAdapter {
    private final CmderLogger logger = CmderLoggerFactory.getSystemLogger();

    public ServerHandshakeHandlerAdapter() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Optional<ProtocolVersion> clientProtocolVersionOptional = readProtocolVersion((ByteBuf) msg);
        if (clientProtocolVersionOptional.isPresent()) {
            logger.debug("Writing server protocol version {} for connection to {}",
                clientProtocolVersionOptional.get(), ctx.channel().remoteAddress());
            writeProtocolVersion(ctx, clientProtocolVersionOptional.get());
        } else {
            ctx.close();
        }
    }

    private Optional<ProtocolVersion> readProtocolVersion(ByteBuf byteBuf) {
        try {
            byte version = byteBuf.readByte();
            return ProtocolVersion.valueOf(version);
        } finally {
            byteBuf.release();
        }
    }
}
