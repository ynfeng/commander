package com.github.ynfeng.commander.cluster.communicate.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

class ServerHandshakeHandlerAdapterTest {

    @Test
    public void should_send_protocol_version_when_client_connected() throws Exception {
        EmbeddedChannel channel = createChannelAndWriteProtocolVersion(1);

        ByteBuf byteBuf = channel.readOutbound();
        assertThat(byteBuf.readByte(), is((byte) 1));
    }

    @Test
    public void should_close_connection_when_version_invalid() {
        EmbeddedChannel channel = createChannelAndWriteProtocolVersion(0);

        assertThat(channel.isOpen(), is(false));
    }

    @Test
    public void should_add_encoder_and_decoder_when_accept_protocol_version() {
        EmbeddedChannel channel = createChannelAndWriteProtocolVersion(1);
        int handlers = channel.pipeline().toMap().entrySet().size();
        ChannelHandler encoder = channel.pipeline().get("encoder");
        ChannelHandler decoder = channel.pipeline().get("decoder");

        assertThat(handlers, is(2));
        assertThat(encoder, instanceOf(MessageEncoderV1.class));
        assertThat(decoder, instanceOf(MessageDecoderV1.class));
    }

    private EmbeddedChannel createChannelAndWriteProtocolVersion(int i) {
        EmbeddedChannel channel = new EmbeddedChannel(true, false);
        channel.pipeline().addLast(new ServerHandshakeHandlerAdapter());
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(i);
        channel.writeOneInbound(buf);
        return channel;
    }
}
