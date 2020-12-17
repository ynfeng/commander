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
        EmbeddedChannel channel = createChannelAndWriteProtocolVersion("test", "test", 1);

        ByteBuf byteBuf = channel.readOutbound();
        assertThat(byteBuf.readInt(), is("test".hashCode()));
        assertThat(byteBuf.readByte(), is((byte) 1));
    }

    @Test
    public void should_close_connection_when_version_invalid() {
        EmbeddedChannel channel = createChannelAndWriteProtocolVersion("test", "test", 0);

        assertThat(channel.isOpen(), is(false));
    }

    @Test
    public void should_close_connection_when_client_communicate_id_was_wrong() {
        EmbeddedChannel channel = createChannelAndWriteProtocolVersion("test", "wrongId", 0);

        assertThat(channel.isOpen(), is(false));
    }

    @Test
    public void should_add_encoder_and_decoder_when_accept_protocol_version() {
        EmbeddedChannel channel = createChannelAndWriteProtocolVersion("test", "test", 1);
        int handlers = channel.pipeline().toMap().entrySet().size();
        ChannelHandler encoder = channel.pipeline().get("encoder");
        ChannelHandler decoder = channel.pipeline().get("decoder");

        assertThat(handlers, is(2));
        assertThat(encoder, instanceOf(MessageEncoderV1.class));
        assertThat(decoder, instanceOf(MessageDecoderV1.class));
    }

    private EmbeddedChannel createChannelAndWriteProtocolVersion(String communicateId, String clientCommunicateId, int version) {
        EmbeddedChannel channel = new EmbeddedChannel(true, false);
        channel.pipeline().addLast(new ServerHandshakeHandlerAdapter(communicateId));
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(clientCommunicateId.hashCode());
        buf.writeByte(version);
        channel.writeOneInbound(buf);
        return channel;
    }
}
