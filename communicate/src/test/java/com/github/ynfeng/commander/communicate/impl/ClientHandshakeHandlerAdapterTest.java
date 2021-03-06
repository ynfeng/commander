package com.github.ynfeng.commander.communicate.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

class ClientHandshakeHandlerAdapterTest {
    private static final String COMMUNICATE_ID = "test";

    private static EmbeddedChannel createChannelAndRegister(String communicateId) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(false, false);
        channel.pipeline().addLast(NettyMessagingService.HANDSHAKE_FRAME_DECODER, new FixedLengthFrameDecoder(5));
        channel.pipeline().addLast(new ClientHandshakeHandlerAdapter(communicateId, new RemoteClientConnection(new EmbeddedChannel(), Executors.newSingleThreadScheduledExecutor()), new CompletableFuture<>()));
        channel.register();
        return channel;
    }

    @Test
    void should_send_protocol_version_when_connected() throws Exception {
        EmbeddedChannel channel = createChannelAndRegister(COMMUNICATE_ID);

        ByteBuf byteBuf = channel.readOutbound();
        assertThat(byteBuf.readInt(), is(COMMUNICATE_ID.hashCode()));
        assertThat(byteBuf.readByte(), is((byte) 1));
    }

    @Test
    void should_close_when_connected_with_wrong_communicate_id() throws Exception {
        EmbeddedChannel channel = createChannelAndRegister(COMMUNICATE_ID);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt("wrongId".hashCode());
        byteBuf.writeByte(1);
        channel.writeInbound(byteBuf);

        assertThat(channel.isOpen(), is(false));
    }

    @Test
    void should_add_encode_and_decoder_when_protocol_accepted() throws Exception {
        EmbeddedChannel channel = createChannelAndRegister(COMMUNICATE_ID);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(COMMUNICATE_ID.hashCode());
        byteBuf.writeByte(1);
        channel.writeInbound(byteBuf);

        int handlers = channel.pipeline().toMap().entrySet().size();
        ChannelHandler encoder = channel.pipeline().get("encoder");
        ChannelHandler decoder = channel.pipeline().get("decoder");
        ChannelHandler frameDecoder = channel.pipeline().get("frameDecoder");

        assertThat(handlers, is(4));
        assertThat(encoder, instanceOf(MessageEncoderV1.class));
        assertThat(decoder, instanceOf(MessageDecoderV1.class));
        assertThat(frameDecoder, instanceOf(MessageFrameDecoderV1.class));
    }

    @Test
    void should_read_half_packet() throws Exception {
        EmbeddedChannel channel = createChannelAndRegister(COMMUNICATE_ID);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(COMMUNICATE_ID.hashCode());
        channel.writeInbound(byteBuf);

        ChannelHandler encoder = channel.pipeline().get("encoder");
        assertThat(encoder, nullValue());
        assertThat(channel.isOpen(), is(true));

        byteBuf = Unpooled.buffer();
        byteBuf.writeByte(1);
        channel.writeInbound(byteBuf);

        encoder = channel.pipeline().get("encoder");
        assertThat(encoder, instanceOf(MessageEncoderV1.class));
        assertThat(channel.isOpen(), is(true));
    }
}
