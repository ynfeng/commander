package com.github.ynfeng.commander.cluster.discovery.impl;

import com.github.ynfeng.commander.cluster.ClusterNode;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryMessage;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;
import com.github.ynfeng.commander.communicate.impl.NettyBroadcastService;
import com.github.ynfeng.commander.serializer.SerializationTypes;
import com.github.ynfeng.commander.serializer.Serializer;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class MulticastDiscoveryProtocol implements NodeDiscoveryProtocol {
    private final CmderLogger logger = CmderLoggerFactory.getSystemLogger();
    private final MulticastDiscoveryConfig config;
    private final AtomicBoolean isStart = new AtomicBoolean();
    private final NettyBroadcastService broadcastService;
    private final Map<NodeDiscoveryMessage.Type, Set<Consumer<ClusterNode>>> listeners = Maps.newConcurrentMap();
    private final Serializer serializer = Serializer.create(SerializationTypes.builder()
        .add(SerializationTypes.BASIC)
        .add(ClusterNode.class)
        .add(NodeDiscoveryMessage.Type.class)
        .add(NodeDiscoveryMessage.class)
        .build());

    public MulticastDiscoveryProtocol(MulticastDiscoveryConfig config) {
        this.config = config;
        broadcastService = new NettyBroadcastService(config.localAddress(), config.groupAddress());
    }

    @Override
    public void start() {
        if (isStart.compareAndSet(false, true)) {
            broadcastService.start();
            listenNodeOnlineMessage();
            broadcastOnlineMessage();
        }
    }

    private void listenNodeOnlineMessage() {
        broadcastService.addListener(NodeDiscoveryMessage.Type.Online.name(), this::notifyListeners);
    }

    private void notifyListeners(byte[] bytes) {
        NodeDiscoveryMessage message = serializer.decode(bytes);
        listeners.get(message.type()).forEach(each -> each.accept(message.node()));
    }

    private void broadcastOnlineMessage() {
        NodeDiscoveryMessage onlineMessage = NodeDiscoveryMessage.createOnlineMessage(config.localNode());
        broadcastService.broadcast(NodeDiscoveryMessage.Type.Online.name(), serializer.encode(onlineMessage));
    }

    @Override
    public void shutdown() {
        if (isStart.compareAndSet(true, false)) {
            broadcastService.shutdown();
        }
    }

    @Override
    public boolean isStarted() {
        return isStart.get();
    }

    @Override
    public void addListener(NodeDiscoveryMessage.Type type, Consumer<ClusterNode> listener) {
        listeners.computeIfAbsent(type, t -> Sets.newCopyOnWriteArraySet()).add(listener);
    }
}
