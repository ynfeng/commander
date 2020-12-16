package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.cluster.communicate.protocol.ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.Optional;

public class ServerHandshakeHandlerAdapter extends AbstractHandshakeHanderAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Optional<ProtocolVersion> clientProtocolVersionOptional = readProtocolVersion((ByteBuf) msg);
        if (clientProtocolVersionOptional.isPresent()) {
            writeProtocolVersion(ctx, clientProtocolVersionOptional.get());
            acceptProtocolVersion(ctx, clientProtocolVersionOptional.get());
        } else {
            ctx.close();
        }
    }

}
