package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

public class MessageDecoderV1 extends AbstractMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ProtocolMessage.Type type = ProtocolMessage.Type.of(in.readByte());
        ProtocolMessage protocolMessage = createByType(type);
        protocolMessage.decode(in);
        out.add(protocolMessage);
    }

    @Override
    public ProtocolMessage createByType(ProtocolMessage.Type type) {
        switch (type) {
            case REQUEST:
                return new ProtocolRequestMessage(type);
            case RESPONSE:
                return new ProtocolResponseMessage(type);
            default:
                throw new IllegalArgumentException("Illegal protocol message type.");
        }
    }
}
