package com.github.ynfeng.commander.cluster.communicate.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.communicate.protocol.ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

class ClientHandshakeHandlerAdapterTest {

    @Test
    public void should_send_protocol_version_when_connected() throws Exception {
        EmbeddedChannel channel = createChannelAndRegister(ProtocolVersion.V1);

        ByteBuf byteBuf = channel.readOutbound();
        assertThat(byteBuf.readByte(), is((byte) 1));
    }

    @Test
    public void should_add_encode_and_decoder_when_protocol_accepted() throws Exception {
        EmbeddedChannel channel = createChannelAndRegister(ProtocolVersion.V1);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeByte(1);
        channel.writeInbound(byteBuf);

        int handlers = channel.pipeline().toMap().entrySet().size();
        ChannelHandler encoder = channel.pipeline().get("encoder");
        ChannelHandler decoder = channel.pipeline().get("decoder");

        assertThat(handlers, is(2));
        assertThat(encoder, instanceOf(MessageEncoderV1.class));
        assertThat(decoder, instanceOf(MessageDecoderV1.class));
    }

    private EmbeddedChannel createChannelAndRegister(ProtocolVersion protocolVersion) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(false, false);
        channel.pipeline().addLast(new ClientHandshakeHandlerAdapter(protocolVersion));
        channel.register();
        return channel;
    }
}
