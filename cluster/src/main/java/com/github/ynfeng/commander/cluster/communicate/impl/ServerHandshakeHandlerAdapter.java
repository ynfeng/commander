package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.Optional;

public class ServerHandshakeHandlerAdapter extends AbstractHandshakeHanderAdapter {
    private final String communicateId;

    public ServerHandshakeHandlerAdapter(String communicateId) {
        this.communicateId = communicateId;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (checkCommunicateIdOrCloseContext(ctx, communicateId, (ByteBuf) msg)) {
            Optional<ProtocolVersion> clientProtocolVersionOptional = readProtocolVersion((ByteBuf) msg);
            if (clientProtocolVersionOptional.isPresent()) {
                accept(ctx, clientProtocolVersionOptional.get());
            } else {
                ctx.close();
            }
        }
    }

    private void accept(ChannelHandlerContext ctx, ProtocolVersion clientProtocolVersion) {
        writeProtocolVersion(communicateId, ctx, clientProtocolVersion);
        acceptProtocolVersion(ctx, clientProtocolVersion);
    }

}
