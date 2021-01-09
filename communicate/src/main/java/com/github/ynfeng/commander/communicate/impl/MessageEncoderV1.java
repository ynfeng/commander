package com.github.ynfeng.commander.communicate.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class MessageEncoderV1 extends AbstractMessageEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ((ProtocolMessage) msg).encode(out);
    }
}
