package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class MessageEncoderV1 extends AbstractMessageEncoder {

    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ((ProtocolMessage) msg).encode(out);
    }
}
