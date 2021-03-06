package com.github.ynfeng.commander.communicate.impl;

import com.github.ynfeng.commander.communicate.BroadcastService;
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
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.slf4j.Logger;

public class NettyBroadcastService extends ManageableSupport implements BroadcastService {
    private static final Logger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final Address groupAddress;
    private final Host localHost;
    private final Map<String, Set<Consumer<byte[]>>> listeners = Maps.newConcurrentMap();
    private final Serializer serializer = Serializer.create(SerializationTypes.builder()
        .add(SerializationTypes.BASIC)
        .add(Message.class)
        .build());
    private final NioEventLoopGroup group;
    private final NetworkInterface iface;
    private DatagramChannel clientChannel;

    public NettyBroadcastService(Host localHost, Address groupAddress) {
        this.localHost = localHost;
        this.groupAddress = groupAddress;
        iface = getIface();
        group = new NioEventLoopGroup(0, Threads.namedThreads("netty-broadcast-event-nio-client-%d", LOGGER));
    }

    @Override
    public void broadcast(String subject, byte[] payload) {
        Message message = new Message(subject, payload);
        byte[] bytes = serializer.encode(message);
        ByteBuf buf = clientChannel.alloc().buffer(4 + bytes.length);
        buf.writeInt(bytes.length).writeBytes(bytes);
        clientChannel.writeAndFlush(new DatagramPacket(buf, groupAddress.toInetSocketAddress()));
    }

    @Override
    public void addListener(String subject, Consumer<byte[]> listener) {
        listeners.computeIfAbsent(subject, k -> Sets.newCopyOnWriteArraySet())
            .add(listener);
    }

    @Override
    public void removeListener(String subject, Consumer<byte[]> listener) {
        Set<Consumer<byte[]>> listenersOfSubject = listeners.getOrDefault(subject, Sets.newCopyOnWriteArraySet());
        listenersOfSubject.remove(listener);
    }

    @Override
    public int numOfSubjectOfListeners(String subject) {
        return listeners.getOrDefault(subject, Sets.newCopyOnWriteArraySet()).size();
    }

    @Override
    public void doStart() {
        bootClient();
        joinGroup();
    }

    private void joinGroup() {
        boolean success = clientChannel.joinGroup(groupAddress.toInetSocketAddress(), iface).isSuccess();
        if (!success) {
            throw new IllegalStateException(
                String.format("%s failed to join group %s on port %d",
                    localHost.ip(), groupAddress.host(), groupAddress.port()));
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} successfully joined multicast group {} on port {}",
                localHost.ip(), groupAddress.host(), groupAddress.port());
        }
    }

    private void bootClient() {
        try {
            clientChannel = (DatagramChannel) clientBootstrap()
                .bind()
                .sync()
                .channel();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Bootstrap clientBootstrap() {
        return new Bootstrap()
            .group(group)
            .channelFactory(() -> new NioDatagramChannel(InternetProtocolFamily.IPv4))
            .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                @Override
                protected void channelRead0(ChannelHandlerContext context, DatagramPacket packet) throws Exception {
                    notifySubcribedListeners(packet);
                }
            })
            .option(ChannelOption.IP_MULTICAST_IF, iface)
            .option(ChannelOption.SO_REUSEADDR, true)
            .localAddress(groupAddress.port());
    }

    private void notifySubcribedListeners(DatagramPacket packet) {
        byte[] payload = new byte[packet.content().readInt()];
        packet.content().readBytes(payload);
        Message message = serializer.decode(payload);
        Set<Consumer<byte[]>> subcribedListeners = listeners.get(message.subject);
        if (subcribedListeners != null) {
            subcribedListeners.forEach(listener -> listener.accept(message.payload));
        }
    }

    private NetworkInterface getIface() {
        try {
            return NetworkInterface.getByInetAddress(localHost.toInetAddress());
        } catch (SocketException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void doShutdown() {
        clientChannel
            .leaveGroup(groupAddress.toInetSocketAddress(), iface)
            .syncUninterruptibly();
        group.shutdownGracefully().syncUninterruptibly();
        LOGGER.debug("Broadcaset service shutdown successfully.");
    }

    static class Message {
        private String subject;
        private byte[] payload;

        @SuppressWarnings("unused")
        protected Message() {
        }

        protected Message(String subject, byte[] payload) {
            this.subject = subject;
            this.payload = payload.clone();
        }
    }
}
