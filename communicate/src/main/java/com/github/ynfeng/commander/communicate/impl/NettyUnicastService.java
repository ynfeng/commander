package com.github.ynfeng.commander.communicate.impl;

import com.github.ynfeng.commander.communicate.UnicastService;
import com.github.ynfeng.commander.serializer.SerializationTypes;
import com.github.ynfeng.commander.serializer.Serializer;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Host;
import com.github.ynfeng.commander.support.ManageableSupport;
import com.github.ynfeng.commander.support.Threads;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultMaxBytesRecvByteBufAllocator;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import org.slf4j.Logger;

public class NettyUnicastService extends ManageableSupport implements UnicastService {
    private final Logger logger = CmderLoggerFactory.getSystemLogger();
    private final Map<String, Set<BiConsumer<Address, byte[]>>> listeners = Maps.newConcurrentMap();
    private final NioEventLoopGroup group;
    private final int port;
    private final Serializer serializer = Serializer.create(SerializationTypes.builder()
        .add(SerializationTypes.BASIC)
        .add(Message.class)
        .add(Address.class)
        .add(Host.class)
        .build());
    private Channel channel;

    public NettyUnicastService(int port) {
        this.port = port;
        group = new NioEventLoopGroup(0, Threads.namedThreads("netty-unicast-event-nio-client-%d", logger));
    }

    @Override
    public void unicast(Address address, String subject, byte[] payload) {
        Message message = new Message(address, subject, payload);
        byte[] bytes = serializer.encode(message);
        ByteBuf buf = channel.alloc().buffer(4 + bytes.length);
        buf.writeInt(bytes.length).writeBytes(bytes);
        channel.writeAndFlush(new DatagramPacket(buf, address.toInetSocketAddress()));
    }

    @Override
    public void addListener(String subject, BiConsumer<Address, byte[]> listener) {
        listeners.computeIfAbsent(subject, k -> Sets.newCopyOnWriteArraySet())
            .add(listener);
    }

    @Override
    public void removeListener(String subject, BiConsumer<Address, byte[]> listener) {
        listeners.getOrDefault(subject, Sets.newCopyOnWriteArraySet())
            .remove(listener);
    }

    @Override
    public int numOfSubjectOfListeners(String subject) {
        return listeners.getOrDefault(subject, Sets.newCopyOnWriteArraySet()).size();
    }

    @Override
    public void doStart() {
        bootServer();
    }

    @Override
    public void doShutdown() {
        channel.close().syncUninterruptibly();
        group.shutdownGracefully().syncUninterruptibly();
    }

    private void bootServer() {
        Bootstrap bootstrap = new Bootstrap()
            .group(group)
            .channel(NioDatagramChannel.class)
            .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                @Override
                protected void channelRead0(ChannelHandlerContext context, DatagramPacket packet) throws Exception {
                    notifySubscribedListeners(packet);
                }
            })
            .option(ChannelOption.RCVBUF_ALLOCATOR, new DefaultMaxBytesRecvByteBufAllocator())
            .option(ChannelOption.SO_BROADCAST, true)
            .option(ChannelOption.SO_REUSEADDR, true);
        bind(bootstrap);
    }

    private void bind(Bootstrap bootstrap) {
        ChannelFuture channelFuture = bootstrap.bind("0.0.0.0", port);
        if (!channelFuture.syncUninterruptibly().isSuccess()) {
            throw new IllegalStateException(
                String.format("Failed to bind UDP server to port %d", port),
                channelFuture.cause());
        }
        channel = channelFuture.syncUninterruptibly().channel();
        logger.info("UDP server listening for connections on port {}", port);
    }

    private void notifySubscribedListeners(DatagramPacket packet) {
        byte[] payload = new byte[packet.content().readInt()];
        packet.content().readBytes(payload);
        Message message = serializer.decode(payload);
        Set<BiConsumer<Address, byte[]>> subcribedListeners = listeners.get(message.subject);
        if (subcribedListeners != null) {
            subcribedListeners.forEach(consumer -> consumer.accept(message.source, message.payload));
        }
    }

    static class Message {
        private Address source;
        private String subject;
        private byte[] payload;

        @SuppressWarnings("unused")
        protected Message() {

        }

        Message(Address source, String subject, byte[] payload) {
            this.source = source;
            this.subject = subject;
            this.payload = payload.clone();
        }
    }
}
