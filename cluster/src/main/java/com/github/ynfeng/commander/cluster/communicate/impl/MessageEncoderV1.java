package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class MessageEncoderV1 extends AbstractMessageEncoder {

    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ProtocolMessage protocolMessage = (ProtocolMessage) msg;
        int len = 11;
        byte[] hostBytes = protocolMessage.address().host().getBytes();
        len += hostBytes.length;
        byte[] subjectBytes = protocolMessage.subject().getBytes();
        len += subjectBytes.length;
        byte[] payload = protocolMessage.payload();
        len += payload.length;
        out.writeInt(len);
        out.writeByte(hostBytes.length);
        out.writeBytes(hostBytes);
        out.writeInt(protocolMessage.address().port());
        out.writeShort(subjectBytes.length);
        out.writeBytes(subjectBytes);
        out.writeInt(payload.length);
        out.writeBytes(payload);
    }
}
