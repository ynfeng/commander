package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.cluster.communicate.protocol.ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.Optional;

public class ServerHandshakeHandlerAdapter extends AbstractHandshakeHanderAdapter {
    public ServerHandshakeHandlerAdapter() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Optional<ProtocolVersion> clientProtocolVersionOptional = readProtocolVersion(byteBuf);
        if (clientProtocolVersionOptional.isPresent()) {
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
