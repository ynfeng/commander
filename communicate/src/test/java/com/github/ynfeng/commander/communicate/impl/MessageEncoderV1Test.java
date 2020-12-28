package com.github.ynfeng.commander.cluster.communicate.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.support.Address;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

class MessageEncoderV1Test {

    @Test
    public void should_encode_protocol_request_message() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast(new MessageEncoderV1());

        ProtocolRequestMessage requestMessage = new ProtocolRequestMessage("test channel", Address.of("127.0.0.1", 1988), "hello".getBytes());
        channel.writeOutbound(requestMessage);

        ByteBuf byteBuf = channel.readOutbound();

        assertThat(byteBuf.readInt(), is(46));
        assertThat(byteBuf.readByte(), is(ProtocolMessage.Type.REQUEST.value()));
        assertThat(byteBuf.readLong(), is(1L));
        assertThat(byteBuf.readByte(), is((byte) 9));

        byte[] host = new byte[9];
        byteBuf.readBytes(host);
        assertThat(host, is("127.0.0.1".getBytes()));

        assertThat(byteBuf.readInt(), is(1988));
        assertThat(byteBuf.readShort(), is((short) 12));

        byte[] subject = new byte[12];
        byteBuf.readBytes(subject);
        assertThat(subject, is("test channel".getBytes()));

        assertThat(byteBuf.readInt(), is(5));

        byte[] payload = new byte[5];
        byteBuf.readBytes(payload);
        assertThat(payload, is("hello".getBytes()));
    }

    @Test
    public void should_encode_protocol_response_message() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast(new MessageEncoderV1());

        ProtocolResponseMessage response = new ProtocolResponseMessage(2L, ProtocolResponseMessage.Status.OK, "hello".getBytes());
        channel.writeOutbound(response);

        ByteBuf byteBuf = channel.readOutbound();

        assertThat(byteBuf.readInt(), is(19));
        assertThat(byteBuf.readByte(), is(ProtocolMessage.Type.RESPONSE.value()));
        assertThat(byteBuf.readLong(), is(2L));
        assertThat(byteBuf.readByte(), is((byte) 1));
        assertThat(byteBuf.readInt(), is(5));
        byte[] payload = new byte[5];
        byteBuf.readBytes(payload);
        assertThat(payload, is("hello".getBytes()));
    }
}
