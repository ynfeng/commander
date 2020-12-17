package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.cluster.communicate.Message;
import com.github.ynfeng.commander.cluster.communicate.MessagingService;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Threads;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

public class NettyMessagingService implements MessagingService {
    private final CmderLogger logger = CmderLoggerFactory.getSystemLogger();
    private final AtomicBoolean started = new AtomicBoolean();
    private final Address localAddress;
    private final String communicateId;
    private Channel serverChannel;
    private EventLoopGroup serverGroup;
    private EventLoopGroup clientGroup;
    private Class<? extends ServerChannel> serverChannelClass;
    private Class<? extends Channel> clientChannelClass;

    public NettyMessagingService(String communicateId, Address localAddress) {
        this.localAddress = localAddress;
        this.communicateId = communicateId;
        initEventLoopGroup();
    }

    private void initEventLoopGroup() {
        if (!tryInitEpollEventLoopGroup()) {
            initNioEventLoopGroup();
        }
    }

    private void initNioEventLoopGroup() {
        clientGroup = new NioEventLoopGroup(0,
            Threads.namedThreads("netty-messaging-event-nio-client-%d", logger));
        serverGroup = new NioEventLoopGroup(0,
            Threads.namedThreads("netty-messaging-event-nio-server-%d", logger));
        serverChannelClass = NioServerSocketChannel.class;
        clientChannelClass = NioSocketChannel.class;
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private boolean tryInitEpollEventLoopGroup() {
        try {
            clientGroup = new EpollEventLoopGroup(0,
                Threads.namedThreads("netty-messaging-event-epoll-client-%d", logger));
            serverGroup = new EpollEventLoopGroup(0,
                Threads.namedThreads("netty-messaging-event-epoll-server-%d", logger));
            serverChannelClass = EpollServerSocketChannel.class;
            clientChannelClass = EpollSocketChannel.class;
            return true;
        } catch (Throwable e) {
            logger.debug("Failed to initialize native (epoll) transport. "
                + "Reason: {}. Proceeding with nio.", e.getMessage());
            return false;
        }
    }

    @Override
    public CompletableFuture<Void> sendAsync(Address address, Message message, boolean keepAlive) {
        return null;
    }

    @Override
    public CompletableFuture<byte[]> sendAndReceive(Address address, Message message, boolean keepAlive) {
        return null;
    }

    @Override
    public void registerHandler(String type, BiFunction<Address, byte[], CompletableFuture<byte[]>> handler) {

    }

    @Override
    public void unregisterHandler(String type) {

    }

    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    public void start() {
        if (started.compareAndSet(false, true)) {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_REUSEADDR, true);
            bootstrap.option(ChannelOption.SO_BACKLOG, 128);
            bootstrap.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
                new WriteBufferWaterMark(8 * 1024, 32 * 1024));
            bootstrap.childOption(ChannelOption.SO_RCVBUF, 1024 * 1024);
            bootstrap.childOption(ChannelOption.SO_SNDBUF, 1024 * 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.group(serverGroup, clientGroup);
            bootstrap.channel(serverChannelClass);
            bootstrap.childHandler(new ChannelInboundHandlerAdapter());
            bind(bootstrap);
        }
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private void bind(ServerBootstrap bootstrap) {
        ChannelFuture channelFuture = bootstrap.bind(localAddress.ip(), localAddress.port());
        boolean success = channelFuture.syncUninterruptibly().isSuccess();
        if (!success) {
            throw new IllegalStateException(
                String.format("Failed to bind TCP server to port %s:%d",
                    localAddress.ip(), localAddress.port()));
        }
        logger.info("TCP server listening for connections on {}:{}", localAddress.ip(), localAddress.port());
        serverChannel = channelFuture.channel();
    }

    @Override
    @SuppressWarnings("checkstyle:MethodLength")
    public void shutdown() {
        if (started.compareAndSet(true, false)) {
            try {
                serverChannel.close().sync();
                serverGroup.shutdownGracefully().syncUninterruptibly();
                clientGroup.shutdownGracefully().syncUninterruptibly();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public boolean isStarted() {
        return started.get();
    }

//    private static class BasicServerChannelInitializer extends ChannelInitializer<SocketChannel> {
//        @Override
//        protected void initChannel(SocketChannel channel) throws Exception {
//            channel.pipeline().addLast("handshake", new ChannelInboundHandlerAdapter());
//        }
//    }
}
