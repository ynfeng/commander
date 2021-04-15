package com.github.ynfeng.commander.cluster.discovery.impl;

import com.github.ynfeng.commander.cluster.ClusterMember;
import com.github.ynfeng.commander.cluster.discovery.ClusterMemberDiscoveryMessage;
import com.github.ynfeng.commander.cluster.discovery.ClusterMemberDiscoveryProtocol;
import com.github.ynfeng.commander.communicate.impl.NettyBroadcastService;
import com.github.ynfeng.commander.serializer.SerializationTypes;
import com.github.ynfeng.commander.serializer.Serializer;
import com.github.ynfeng.commander.support.ManageableSupport;
import com.github.ynfeng.commander.support.Threads;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MulticastDiscoveryProtocol extends ManageableSupport implements ClusterMemberDiscoveryProtocol {
    private final CmderLogger logger = CmderLoggerFactory.getSystemLogger();
    private final MulticastDiscoveryProtocolConfig config;
    private final NettyBroadcastService broadcastService;
    private final Map<ClusterMemberDiscoveryMessage.Type, Set<Consumer<ClusterMember>>> listeners
        = Maps.newConcurrentMap();
    private final ScheduledExecutorService scheduledExecutorService;
    private final Serializer serializer = Serializer.create(SerializationTypes.builder()
        .add(SerializationTypes.BASIC)
        .add(ClusterMember.class)
        .add(ClusterMemberDiscoveryMessage.Type.class)
        .add(ClusterMemberDiscoveryMessage.class)
        .build());

    public MulticastDiscoveryProtocol(MulticastDiscoveryProtocolConfig config) {
        this.config = config;
        broadcastService = new NettyBroadcastService(config.localHost(), config.groupAddress());
        scheduledExecutorService = Executors.newScheduledThreadPool(
            1,
            Threads.namedThreads("multicast-discovery-schedule-thread-%d", logger));
    }

    @Override
    public void doStart() {
        broadcastService.start();
        listenNodeChangeMessage();
        broadcastOnlineMessage();
        scheduledExecutorService.scheduleAtFixedRate(
            this::broadcastOnlineMessage, 0, config.broadcastInterval(), TimeUnit.SECONDS);
        logger.debug("Multicast discovery protocol start successfully.");
    }

    private void listenNodeChangeMessage() {
        broadcastService.addListener(ClusterMemberDiscoveryMessage.Type.ONLINE.value(), this::notifyListeners);
        broadcastService.addListener(ClusterMemberDiscoveryMessage.Type.OFFLINE.value(), this::notifyListeners);
    }

    private void notifyListeners(byte[] bytes) {
        ClusterMemberDiscoveryMessage message = serializer.decode(bytes);
        Set<Consumer<ClusterMember>> consumers = listeners.get(message.type());
        if (consumers != null) {
            consumers.forEach(each -> each.accept(message.node()));
        }
    }

    private void broadcastOnlineMessage() {
        ClusterMemberDiscoveryMessage onlineMessage
            = ClusterMemberDiscoveryMessage.createOnlineMessage(config.localMember());
        broadcastService.broadcast(ClusterMemberDiscoveryMessage.Type.ONLINE.value(), serializer.encode(onlineMessage));
        logger.debug("broadcast cluster node online message {}", onlineMessage);
    }

    @Override
    public void doShutdown() {
        broadcastService.shutdown();
        scheduledExecutorService.shutdownNow();
        logger.debug("Multicast discovery protocol shutdown successfully.");
    }

    @Override
    public void addClusterNodeChangeListener(ClusterMemberDiscoveryMessage.Type type,
                                             Consumer<ClusterMember> listener) {
        listeners.computeIfAbsent(type, t -> Sets.newCopyOnWriteArraySet()).add(listener);
    }

    @Override
    public void broadcastOffline() {
        ClusterMemberDiscoveryMessage offlineMessage
            = ClusterMemberDiscoveryMessage.createOfflineMessage(config.localMember());
        byte[] encode = serializer.encode(offlineMessage);
        broadcastService.broadcast(ClusterMemberDiscoveryMessage.Type.OFFLINE.value(), encode);
        logger.debug("broadcast cluster node offline message {}", offlineMessage);
    }
}
