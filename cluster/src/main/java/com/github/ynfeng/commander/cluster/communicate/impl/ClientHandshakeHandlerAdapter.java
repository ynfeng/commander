package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.CompletableFuture;

public class ClientHandshakeHandlerAdapter extends AbstractHandshakeHanderAdapter {
    private final String communicateId;
    private final CompletableFuture<Void> handshakeFuture;
    private final Connection connection;

    public ClientHandshakeHandlerAdapter(String communicateId,
                                         Connection connection,
                                         CompletableFuture<Void> handshakeFuture) {
        this.communicateId = communicateId;
        this.handshakeFuture = handshakeFuture;
        this.connection = connection;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        writeProtocolVersion(communicateId, ctx, ProtocolVersion.lastest());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        if (checkCommunicateIdOrCloseContext(ctx, communicateId, byteBuf)) {
            readProtocolVersion(byteBuf)
                .ifPresent(version -> {
                    acceptProtocolVersion(ctx, connection, version);
                    handshakeFuture.complete(null);
                });
        }
    }

}
