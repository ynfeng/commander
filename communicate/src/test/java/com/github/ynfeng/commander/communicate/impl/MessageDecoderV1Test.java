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

    @Test
    public void should_decode_request_protocol_message() {
        EmbeddedChannel channel = createEmbeddedChannel();
        ByteBuf byteBuf = buildRequestMessageByteBuff();
        channel.writeInbound(byteBuf);

        ProtocolRequestMessage request = channel.readInbound();
        assertRequestMessage(request);
    }

    private static void assertRequestMessage(ProtocolRequestMessage request) {
        assertThat(request.type(), is(ProtocolMessage.Type.REQUEST));
        assertThat(request.messageId(), is(4L));
        assertThat(request.senderAddress(), is(Address.of("127.0.0.1", 1988)));
        assertThat(request.payload(), is("hello".getBytes()));
        assertThat(request.subject(), is("test channel"));
    }

    @Test
    public void should_decode_response_protocol_message() {
        EmbeddedChannel channel = createEmbeddedChannel();
        ByteBuf byteBuf = buildResponseMessageByteBuff();
        channel.writeInbound(byteBuf);

        ProtocolResponseMessage response = channel.readInbound();
        assertResponseMessage(response);
    }

    private static void assertResponseMessage(ProtocolResponseMessage response) {
        assertThat(response.messageId(), is(10L));
        assertThat(response.status(), is(ProtocolResponseMessage.Status.OK));
        assertThat(response.payload(), is("hello".getBytes()));
    }


    @Test
    public void should_decode_half_protocol_request_message() {
        EmbeddedChannel channel = createEmbeddedChannel();
        ByteBuf byteBuf = buildHalfRequestMessageByteBuff();
        channel.writeInbound(byteBuf);
        ProtocolRequestMessage request = channel.readInbound();
        assertThat(request, nullValue());

        byteBuf = fillHalfRequestMessage();
        channel.writeInbound(byteBuf);
        request = channel.readInbound();
        assertRequestMessage(request);
    }

    @Test
    public void should_decode_half_protocol_response_message() {
        EmbeddedChannel channel = createEmbeddedChannel();
        ByteBuf byteBuf = buildHalfResponseMessageByteBuff();
        channel.writeInbound(byteBuf);
        ProtocolResponseMessage response = channel.readInbound();
        assertThat(response, nullValue());

        byteBuf = fillHalfResponseMessage();
        channel.writeInbound(byteBuf);
        response = channel.readInbound();
        assertResponseMessage(response);
    }

    private static ByteBuf buildHalfResponseMessageByteBuff() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(19);
        byteBuf.writeByte(ProtocolMessage.Type.RESPONSE.value());
        byteBuf.writeLong(10L);
        return byteBuf;
    }

    private static ByteBuf fillHalfResponseMessage() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeByte(ProtocolResponseMessage.Status.OK.value());
        byteBuf.writeInt(5);
        byteBuf.writeBytes("hello".getBytes());
        return byteBuf;
    }


    @Test
    public void should_decode_multiple_protocol_request_message() {
        EmbeddedChannel channel = createEmbeddedChannel();
        ByteBuf message1 = buildRequestMessageByteBuff();
        ByteBuf message2 = buildRequestMessageByteBuff();
        channel.writeInbound(message1);
        channel.writeInbound(message2);

        ProtocolRequestMessage protocolRequestMessage1 = channel.readInbound();
        assertRequestMessage(protocolRequestMessage1);
        ProtocolRequestMessage protocolRequestMessage2 = channel.readInbound();
        assertRequestMessage(protocolRequestMessage2);
        ProtocolMessage protocolMessage3 = channel.readOutbound();
        assertThat(protocolMessage3, nullValue());
    }

    @Test
    public void should_decode_multiple_protocol_response_message() {
        EmbeddedChannel channel = createEmbeddedChannel();
        ByteBuf message1 = buildResponseMessageByteBuff();
        ByteBuf message2 = buildResponseMessageByteBuff();
        channel.writeInbound(message1);
        channel.writeInbound(message2);

        ProtocolResponseMessage protocolResponseMessage1 = channel.readInbound();
        assertResponseMessage(protocolResponseMessage1);
        ProtocolResponseMessage protocolResponseMessage2 = channel.readInbound();
        assertResponseMessage(protocolResponseMessage2);
        ProtocolMessage protocolMessage3 = channel.readOutbound();
        assertThat(protocolMessage3, nullValue());
    }

    private static EmbeddedChannel createEmbeddedChannel() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast(new MessageFrameDecoderV1());
        channel.pipeline().addLast(new MessageDecoderV1());
        return channel;
    }

    private static ByteBuf buildRequestMessageByteBuff() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(46);
        byteBuf.writeByte(ProtocolMessage.Type.REQUEST.value());
        byteBuf.writeLong(4L);
        byteBuf.writeByte("127.0.0.1".length());
        byteBuf.writeBytes("127.0.0.1".getBytes());
        byteBuf.writeInt(1988);
        byteBuf.writeShort("test channel".length());
        byteBuf.writeBytes("test channel".getBytes());
        byteBuf.writeInt("hello".length());
        byteBuf.writeBytes("hello".getBytes());
        return byteBuf;
    }

    private static ByteBuf buildResponseMessageByteBuff() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(19);
        byteBuf.writeByte(ProtocolMessage.Type.RESPONSE.value());
        byteBuf.writeLong(10L);
        byteBuf.writeByte(ProtocolResponseMessage.Status.OK.value());
        byteBuf.writeInt(5);
        byteBuf.writeBytes("hello".getBytes());
        return byteBuf;
    }

    private static ByteBuf buildHalfRequestMessageByteBuff() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(46);
        byteBuf.writeByte(ProtocolMessage.Type.REQUEST.value());
        byteBuf.writeLong(4L);
        byteBuf.writeByte("127.0.0.1".length());
        return byteBuf;
    }

    private ByteBuf fillHalfRequestMessage() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes("127.0.0.1".getBytes());
        byteBuf.writeInt(1988);
        byteBuf.writeShort("test channel".length());
        byteBuf.writeBytes("test channel".getBytes());
        byteBuf.writeInt("hello".length());
        byteBuf.writeBytes("hello".getBytes());
        return byteBuf;
    }
}
