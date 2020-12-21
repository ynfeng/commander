package com.github.ynfeng.commander.cluster.communicate.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import org.junit.jupiter.api.Test;

class ServerHandshakeHandlerAdapterTest {

    private static EmbeddedChannel createChannel(String communicateId) {
        EmbeddedChannel channel = new EmbeddedChannel(true, false);
        channel.pipeline().addLast(NettyMessagingService.HANDSHAKE_FRAME_DECODER, new FixedLengthFrameDecoder(5));
        channel.pipeline().addLast(new ServerHandshakeHandlerAdapter(communicateId, new ServerConnection(new EmbeddedChannel(), new Handlers())));
        return channel;
    }

    private static void writeProtocolVersion(String clientCommunicateId, int version, EmbeddedChannel channel) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(clientCommunicateId.hashCode());
        buf.writeByte(version);
        channel.writeOneInbound(buf);
    }

    @Test
    public void should_send_protocol_version_when_client_connected() throws Exception {
        EmbeddedChannel channel = createChannel("test");
        writeProtocolVersion("test", 1, channel);

        ByteBuf byteBuf = channel.readOutbound();
        assertThat(byteBuf.readInt(), is("test".hashCode()));
        assertThat(byteBuf.readByte(), is((byte) 1));
    }

    @Test
    public void should_read_half_packet() {
        EmbeddedChannel channel = createChannel("test");
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt("test".hashCode());
        channel.writeInbound(buf);

        ChannelHandler encoder = channel.pipeline().get("encoder");
        assertThat(channel.isOpen(), is(true));
        assertThat(encoder, nullValue());

        buf = Unpooled.buffer();
        buf.writeByte(1);
        channel.writeInbound(buf);

        encoder = channel.pipeline().get("encoder");
        ChannelHandler decoder = channel.pipeline().get("decoder");
        ChannelHandler frameDecoder = channel.pipeline().get("frameDecoder");
        assertThat(encoder, instanceOf(MessageEncoderV1.class));
        assertThat(decoder, instanceOf(MessageDecoderV1.class));
        assertThat(frameDecoder, instanceOf(MessageFrameDecoderV1.class));
    }

    @Test
    public void should_close_connection_when_version_invalid() {
        EmbeddedChannel channel = createChannel("test");
        writeProtocolVersion("test", 0, channel);

        assertThat(channel.isOpen(), is(false));
    }

    @Test
    public void should_close_connection_when_client_communicate_id_was_wrong() {
        EmbeddedChannel channel = createChannel("test");
        writeProtocolVersion("wrongId", 0, channel);

        assertThat(channel.isOpen(), is(false));
    }

    @Test
    public void should_add_encoder_and_decoder_when_accept_protocol_version() {
        EmbeddedChannel channel = createChannel("test");
        writeProtocolVersion("test", 1, channel);
        int handlers = channel.pipeline().toMap().entrySet().size();
        ChannelHandler encoder = channel.pipeline().get("encoder");
        ChannelHandler decoder = channel.pipeline().get("decoder");
        ChannelHandler frameDecoder = channel.pipeline().get("frameDecoder");

        assertThat(handlers, is(4));
        assertThat(encoder, instanceOf(MessageEncoderV1.class));
        assertThat(decoder, instanceOf(MessageDecoderV1.class));
        assertThat(frameDecoder, instanceOf(MessageFrameDecoderV1.class));
    }
}
