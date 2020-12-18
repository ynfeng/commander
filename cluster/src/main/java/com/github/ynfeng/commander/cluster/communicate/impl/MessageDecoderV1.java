package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.support.Address;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

public class MessageDecoderV1 extends AbstractMessageDecoder {
    private static ProtocolMessage buildProtocolMessage(Address address, String subject, byte[] payload) {
        return ProtocolMessage.builder()
            .address(address)
            .subject(subject)
            .payload(payload)
            .build();
    }

    private static Address readAddress(ByteBuf in) {
        byte hostLen = in.readByte();
        byte[] hostBytes = new byte[hostLen];
        in.readBytes(hostBytes);
        int port = in.readInt();
        String host = new String(hostBytes);
        return Address.of(host, port);
    }

    private static String readSubject(ByteBuf in) {
        short subjectLen = in.readShort();
        byte[] subjectBytes = new byte[subjectLen];
        in.readBytes(subjectBytes);
        return new String(subjectBytes);
    }

    private static byte[] readPayload(ByteBuf in) {
        int payloadLen = in.readInt();
        byte[] payload = new byte[payloadLen];
        in.readBytes(payload);
        return payload;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Address address = readAddress(in);
        String subject = readSubject(in);
        byte[] payload = readPayload(in);
        out.add(buildProtocolMessage(address, subject, payload));
    }
}
