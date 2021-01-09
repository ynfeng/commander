package com.github.ynfeng.commander.cluster.discovery.impl;

import com.github.ynfeng.commander.cluster.ClusterNode;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryMessage;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;
import com.github.ynfeng.commander.communicate.impl.NettyBroadcastService;
import com.github.ynfeng.commander.serializer.SerializationTypes;
import com.github.ynfeng.commander.serializer.Serializer;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class MulticastDiscoveryProtocol implements NodeDiscoveryProtocol {
    private final CmderLogger logger = CmderLoggerFactory.getSystemLogger();
    private final MulticastDiscoveryConfig config;
    private final AtomicBoolean isStart = new AtomicBoolean();
    private final NettyBroadcastService broadcastService;
    private final Map<NodeDiscoveryMessage.Type, Set<Consumer<ClusterNode>>> listeners = Maps.newConcurrentMap();
    private final ScheduledExecutorService scheduledExecutorService;
    private final Serializer serializer = Serializer.create(SerializationTypes.builder()
        .add(SerializationTypes.BASIC)
        .add(ClusterNode.class)
        .add(NodeDiscoveryMessage.Type.class)
        .add(NodeDiscoveryMessage.class)
        .build());

    public MulticastDiscoveryProtocol(MulticastDiscoveryConfig config) {
        this.config = config;
        broadcastService = new NettyBroadcastService(config.localHost(), config.groupAddress());
        scheduledExecutorService = Executors.newScheduledThreadPool(
            1,
            Threads.namedThreads("multicast-discovery-schedule-thread-%d", logger));
    }

    @Override
    public void start() {
        if (isStart.compareAndSet(false, true)) {
            broadcastService.start();
            listenNodeChangeMessage();
            broadcastOnlineMessage();
            scheduledExecutorService.scheduleAtFixedRate(
                () -> broadcastOnlineMessage(), 5, config.broadcastInterval(), TimeUnit.SECONDS);
            logger.debug("Multicast discovery protocol start successfully.");
        }
    }

    private void listenNodeChangeMessage() {
        broadcastService.addListener(NodeDiscoveryMessage.Type.Online.value(), this::notifyListeners);
        broadcastService.addListener(NodeDiscoveryMessage.Type.Offline.value(), this::notifyListeners);
    }

    private void notifyListeners(byte[] bytes) {
        NodeDiscoveryMessage message = serializer.decode(bytes);
        Set<Consumer<ClusterNode>> consumers = listeners.get(message.type());
        if (consumers != null) {
            consumers.forEach(each -> each.accept(message.node()));
        }
    }

    private void broadcastOnlineMessage() {
        NodeDiscoveryMessage onlineMessage = NodeDiscoveryMessage.createOnlineMessage(config.localNode());
        broadcastService.broadcast(NodeDiscoveryMessage.Type.Online.value(), serializer.encode(onlineMessage));
        logger.debug("broadcast cluster node online message {}", onlineMessage.toString());
    }

    @Override
    public void shutdown() {
        if (isStart.compareAndSet(true, false)) {
            broadcastOffline();
            broadcastService.shutdown();
            scheduledExecutorService.shutdownNow();
            logger.debug("Multicast discovery protocol shutdown successfully.");
        }
    }

    @Override
    public boolean isStarted() {
        return isStart.get();
    }

    @Override
    public void addClusterNodeChangeListener(NodeDiscoveryMessage.Type type, Consumer<ClusterNode> listener) {
        listeners.computeIfAbsent(type, t -> Sets.newCopyOnWriteArraySet()).add(listener);
    }

    @Override
    public void broadcastOffline() {
        NodeDiscoveryMessage offlineMessage = NodeDiscoveryMessage.createOfflineMessage(config.localNode());
        broadcastService.broadcast(NodeDiscoveryMessage.Type.Offline.value(), serializer.encode(offlineMessage));
    }
}
