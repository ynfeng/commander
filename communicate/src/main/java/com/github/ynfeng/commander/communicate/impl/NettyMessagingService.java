package com.github.ynfeng.commander.communicate.impl;

import static com.github.ynfeng.commander.support.Threads.namedThreads;

import com.github.ynfeng.commander.communicate.CommunicateException;
import com.github.ynfeng.commander.communicate.MessagingService;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.ManageableSupport;
import com.github.ynfeng.commander.support.OS;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.collect.Maps;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
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
import io.netty.handler.codec.FixedLengthFrameDecoder;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.slf4j.Logger;

public class NettyMessagingService extends ManageableSupport implements MessagingService {
    public static final String HANDSHAKE_FRAME_DECODER = "frameDecoder";
    private static final Logger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final Address localAddress;
    private final String communicateId;
    private final Map<Channel, RemoteClientConnection> clientConnections = Maps.newConcurrentMap();
    private final Handlers handlers = new Handlers();
    private final Channels channels = new Channels();
    private final ScheduledExecutorService timeoutExecutor;
    private Channel serverChannel;
    private EventLoopGroup serverGroup;
    private EventLoopGroup clientGroup;
    private Class<? extends ServerChannel> serverChannelClass;
    private Class<? extends Channel> clientChannelClass;

    public NettyMessagingService(String communicateId, Address localAddress) {
        this.localAddress = localAddress;
        this.communicateId = communicateId;
        initEventLoopGroup();
        timeoutExecutor = Executors.newScheduledThreadPool(
            4, namedThreads("netty-messaging-timeout-%d", LOGGER));
    }

    private void initEventLoopGroup() {
        if (OS.isLinux()) {
            initEpollEventLoopGroup();
        } else {
            initNioEventLoopGroup();
        }
    }

    private void initNioEventLoopGroup() {
        clientGroup = new NioEventLoopGroup(0,
            namedThreads("netty-messaging-event-nio-client-%d", LOGGER));
        serverGroup = new NioEventLoopGroup(0,
            namedThreads("netty-messaging-event-nio-server-%d", LOGGER));
        serverChannelClass = NioServerSocketChannel.class;
        clientChannelClass = NioSocketChannel.class;
    }

    private void initEpollEventLoopGroup() {
        try {
            clientGroup = new EpollEventLoopGroup(0,
                namedThreads("netty-messaging-event-epoll-client-%d", LOGGER));
            serverGroup = new EpollEventLoopGroup(0,
                namedThreads("netty-messaging-event-epoll-server-%d", LOGGER));
            serverChannelClass = EpollServerSocketChannel.class;
            clientChannelClass = EpollSocketChannel.class;
        } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Failed to initialize native (epoll) transport. "
                    + "Reason: {}. Proceeding with nio.", e.getMessage());
            }
            throw new CommunicateException(e);
        }
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    @Override
    public CompletableFuture<byte[]> sendAndReceive(Address address,
                                                    Message message,
                                                    Duration timeout,
                                                    boolean keepAlive) {
        return obtainChannel(address, keepAlive)
            .thenCompose(channel ->
                doSend(message, keepAlive,
                    () -> getOrCreateRemoteClientConnection(address, channel),
                    (conn, protocolMessage) -> conn.sendAndReceive(protocolMessage, timeout)));
    }

    @Override
    public CompletableFuture<Void> sendAsync(Address address, Message message, boolean keepAlive) {
        return obtainChannel(address, keepAlive).thenCompose(channel ->
            doSend(message, keepAlive,
                () -> getOrCreateRemoteClientConnection(address, channel),
                (conn, protocolMessage) -> conn.sendAsync(protocolMessage)));
    }

    private CompletableFuture<Channel> obtainChannel(Address address, boolean keepAlive) {
        if (keepAlive) {
            return channels.get(address, () -> openChannel(address));
        }
        return openChannel(address);
    }

    @SuppressWarnings( {"checkstyle:LineLength", "checkstyle:ParameterNumber"})
    private <T> CompletableFuture<T> doSend(Message message,
                                            boolean keepAlive,
                                            Supplier<ClientConnection> connectionSupplier,
                                            BiFunction<ClientConnection, ProtocolRequestMessage, CompletableFuture<T>> sendFunction) {
        CompletableFuture<T> future = new CompletableFuture<>();
        ProtocolRequestMessage request = new ProtocolRequestMessage(message.subject(), localAddress, message.payload());
        ClientConnection conn = connectionSupplier.get();
        sendFunction.apply(conn, request).whenComplete(completeSend(keepAlive, future, conn));
        return future;
    }

    private static <T> BiConsumer<T, Throwable> completeSend(boolean keepAlive, CompletableFuture<T> future, ClientConnection conn) {
        return (r, t) -> {
            tryCloseConnection(conn, keepAlive);
            completeSendFuture(future, r, t);
        };
    }

    private static void tryCloseConnection(ClientConnection conn, boolean keepAlive) {
        if (!keepAlive) {
            conn.close();
        }
    }

    private static <T> void completeSendFuture(CompletableFuture<T> future, T r, Throwable t) {
        if (t != null) {
            future.completeExceptionally(t);
        } else {
            future.complete(r);
        }
    }

    private ClientConnection getOrCreateRemoteClientConnection(Address address, Channel channel) {
        RemoteClientConnection conn = clientConnections.computeIfAbsent(channel,
            c -> new RemoteClientConnection(c, timeoutExecutor));
        channel.closeFuture().addListener(f -> {
            clientConnections.remove(channel);
            channels.remove(address);
        });
        return conn;
    }

    private CompletableFuture<Channel> openChannel(Address address) {
        CompletableFuture<Void> handshakeFuture = new CompletableFuture<>();
        Bootstrap bootstrap = new Bootstrap();
        setClientOptions(bootstrap);
        bootstrap.group(clientGroup);
        bootstrap.channel(clientChannelClass);
        initClientHandlers(address, handshakeFuture, bootstrap);
        ChannelFuture connectFuture = bootstrap.connect(address.toInetSocketAddress());
        return handshakeFuture.thenApply(v -> connectFuture.channel());
    }

    private void initClientHandlers(Address address, CompletableFuture<Void> handshakeFuture, Bootstrap bootstrap) {
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ClientConnection clientConnection = getOrCreateRemoteClientConnection(address, ch);
                ch.pipeline().addLast(HANDSHAKE_FRAME_DECODER,
                    new FixedLengthFrameDecoder(5));
                ch.pipeline().addLast(
                    new ClientHandshakeHandlerAdapter(communicateId, clientConnection, handshakeFuture));
            }
        });
    }

    private static void setClientOptions(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.option(ChannelOption.WRITE_BUFFER_WATER_MARK,
            new WriteBufferWaterMark(10 * 32 * 1024, 10 * 64 * 1024));
        bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 1024);
        bootstrap.option(ChannelOption.SO_SNDBUF, 1024 * 1024);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
    }

    @Override
    public void registerHandler(String type, BiConsumer<Address, byte[]> handler) {
        handlers.add(type, (connection, message) ->
            handler.accept(message.senderAddress(), message.payload()));
    }

    @Override
    public void registerHandler(String type, BiFunction<Address, byte[], byte[]> handler) {
        handlers.add(type, (connection, message) -> {
            byte[] reply = handler.apply(message.senderAddress(), message.payload());
            ProtocolResponseMessage ok = ProtocolResponseMessage.ok(message.messageId(), reply);
            connection.reply(ok);
        });
    }

    @Override
    public void unregisterHandler(String type) {
        handlers.remove(type);
    }

    @Override
    public void doStart() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        setServerOptions(bootstrap);
        bootstrap.group(serverGroup, clientGroup);
        bootstrap.channel(serverChannelClass);
        initServerHandlers(bootstrap);
        bind(bootstrap);
    }

    private void initServerHandlers(ServerBootstrap bootstrap) {
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(HANDSHAKE_FRAME_DECODER,
                    new FixedLengthFrameDecoder(5));
                ch.pipeline().addLast(
                    new ServerHandshakeHandlerAdapter(communicateId, new RemoteServerConnection(ch, handlers)));
            }
        });
    }

    private static void setServerOptions(ServerBootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
            new WriteBufferWaterMark(8 * 1024, 32 * 1024));
        bootstrap.childOption(ChannelOption.SO_RCVBUF, 1024 * 1024);
        bootstrap.childOption(ChannelOption.SO_SNDBUF, 1024 * 1024);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    private void bind(ServerBootstrap bootstrap) {
        ChannelFuture channelFuture = bootstrap.bind(localAddress.host(), localAddress.port());
        boolean success = channelFuture.syncUninterruptibly().isSuccess();
        if (!success) {
            throw new IllegalStateException(
                String.format("Failed to bind TCP server to port %s:%d",
                    localAddress.host(), localAddress.port()));
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("TCP server listening for connections on {}:{}", localAddress.host(), localAddress.port());
        }
        serverChannel = channelFuture.channel();
    }

    @Override
    public void doShutdown() {
        serverChannel.close();
        serverGroup.shutdownGracefully();
        clientGroup.shutdownGracefully();
    }
}
