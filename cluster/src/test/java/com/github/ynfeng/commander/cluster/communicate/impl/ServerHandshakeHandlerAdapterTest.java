package com.github.ynfeng.commander.cluster.communicate.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

class ServerHandshakeHandlerAdapterTest {

    @Test
    public void should_send_protocol_version_when_client_connected() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(true, false);
        channel.pipeline().addLast(new ServerHandshakeHandlerAdapter());
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(1);
        channel.writeOneInbound(buf);

        ByteBuf byteBuf = channel.readOutbound();
        assertThat(byteBuf.readByte(), is((byte) 1));
    }

    @Test
    public void should_close_connection_when_version_invalid() {
        EmbeddedChannel channel = new EmbeddedChannel(true, false);
        channel.pipeline().addLast(new ServerHandshakeHandlerAdapter());
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0);
        channel.writeOneInbound(buf);

        assertThat(channel.isOpen(), is(false));
    }
}
