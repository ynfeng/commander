package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.support.Address;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NettyMessagingServiceTest {
    private NettyMessagingService messagingService;

    @BeforeEach
    public void setup() {
        messagingService = new NettyMessagingService("test", Address.of("127.0.0.1", 7892));
        messagingService.start();
    }

    @AfterEach
    public void destory() {
        messagingService.shutdown();
    }

    @Test
    public void should_response_protocol_version_when_client_connected() {
        Channel channel = connect(Address.of("127.0.0.1", 7892));
        channel.close().syncUninterruptibly();
    }

    private Channel connect(Address address) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(@NotNull SocketChannel ch) throws Exception {
            }
        });

        ChannelFuture f = bootstrap.connect(address.host(), address.port()).syncUninterruptibly();
        return f.channel();
    }

}
