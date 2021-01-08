package com.github.ynfeng.commander.cluster.discovery.impl;

import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;
import com.github.ynfeng.commander.communicate.impl.NettyBroadcastService;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public class MulticastDiscoveryProtocol implements NodeDiscoveryProtocol {
    private final CmderLogger logger = CmderLoggerFactory.getSystemLogger();
    private final MulticastDiscoveryConfig config;
    private final AtomicBoolean isStart = new AtomicBoolean();
    private final NettyBroadcastService broadcastService;

    public MulticastDiscoveryProtocol(MulticastDiscoveryConfig config) {
        this.config = config;
        broadcastService = new NettyBroadcastService(config.localAddress(), config.groupAddress());
    }

    @Override
    public void start() {
        if (isStart.compareAndSet(false, true)) {
            broadcastService.start();
        }
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
}
