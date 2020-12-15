package com.github.ynfeng.commander.cluster.communicate.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.communicate.protocol.ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

class ClientHandshakeHandlerAdapterTest {

    @Test
    public void should_send_protocol_version_when_connected() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(false, false);
        channel.pipeline().addLast(new ClientHandshakeHandlerAdapter(ProtocolVersion.V1));
        channel.register();

        ByteBuf byteBuf = channel.readOutbound();
        assertThat(byteBuf.readByte(), is((byte) 1));
    }

}
