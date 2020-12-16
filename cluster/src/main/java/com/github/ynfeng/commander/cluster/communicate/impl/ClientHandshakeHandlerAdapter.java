package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.cluster.communicate.protocol.ProtocolVersion;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandshakeHandlerAdapter extends AbstractHandshakeHanderAdapter {
    private final CmderLogger logger = CmderLoggerFactory.getSystemLogger();
    private final ProtocolVersion protocolVersion;

    public ClientHandshakeHandlerAdapter(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Writing client protocol version {} for connection to {}",
            protocolVersion, ctx.channel().remoteAddress());
        writeProtocolVersion(ctx, protocolVersion);
    }
}
