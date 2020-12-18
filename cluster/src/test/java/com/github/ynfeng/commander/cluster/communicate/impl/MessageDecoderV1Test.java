package com.github.ynfeng.commander.cluster.communicate.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.support.Address;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

class MessageDecoderV1Test {

    private static void assertMessage(ProtocolMessage protocolMessage) {
        assertThat(protocolMessage.address(), is(Address.of("127.0.0.1", 1988)));
        assertThat(protocolMessage.payload(), is("hello".getBytes()));
        assertThat(protocolMessage.subject(), is("test channel"));
    }

    @Test
    public void should_decode_protocol_message() {
        EmbeddedChannel channel = createEmbeddedChannel();
        ByteBuf byteBuf = buildMessageByteBuff();
        channel.writeInbound(byteBuf);

        ProtocolMessage protocolMessage = channel.readInbound();

        assertMessage(protocolMessage);
    }

    @Test
    public void should_decode_half_protocol_message() {
        EmbeddedChannel channel = createEmbeddedChannel();
        ByteBuf byteBuf = buildHalfMessageByteBuff();
        channel.writeInbound(byteBuf);
        ProtocolMessage protocolMessage = channel.readInbound();
        assertThat(protocolMessage, nullValue());

        byteBuf = fillHalfMessage();
        channel.writeInbound(byteBuf);
        protocolMessage = channel.readInbound();
        assertMessage(protocolMessage);
    }

    @Test
    public void should_decode_multiple_protocol_message() {
        EmbeddedChannel channel = createEmbeddedChannel();
        ByteBuf message1 = buildMessageByteBuff();
        ByteBuf message2 = buildMessageByteBuff();
        channel.writeInbound(message1);
        channel.writeInbound(message2);

        ProtocolMessage protocolMessage1 = channel.readInbound();
        assertMessage(protocolMessage1);
        ProtocolMessage protocolMessage2 = channel.readInbound();
        assertMessage(protocolMessage2);
        ProtocolMessage protocolMessage3 = channel.readOutbound();
        assertThat(protocolMessage3, nullValue());
    }

    private EmbeddedChannel createEmbeddedChannel() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast(new MessageFrameDecoderV1());
        channel.pipeline().addLast(new MessageDecoderV1());
        return channel;
    }

    private ByteBuf buildMessageByteBuff() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(37);
        byteBuf.writeByte("127.0.0.1".length()); // 1
        byteBuf.writeBytes("127.0.0.1".getBytes()); // 9
        byteBuf.writeInt(1988); // 4
        byteBuf.writeShort("test channel".length()); // 2
        byteBuf.writeBytes("test channel".getBytes()); // 12
        byteBuf.writeInt("hello".length()); // 4
        byteBuf.writeBytes("hello".getBytes()); // 5
        return byteBuf;
    }

    private ByteBuf buildHalfMessageByteBuff() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(37);
        byteBuf.writeByte("127.0.0.1".length());
        byteBuf.writeBytes("127.0.0.1".getBytes());
        return byteBuf;
    }

    private ByteBuf fillHalfMessage() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(1988);
        byteBuf.writeShort("test channel".length());
        byteBuf.writeBytes("test channel".getBytes());
        byteBuf.writeInt("hello".length());
        byteBuf.writeBytes("hello".getBytes());
        return byteBuf;
    }
}
