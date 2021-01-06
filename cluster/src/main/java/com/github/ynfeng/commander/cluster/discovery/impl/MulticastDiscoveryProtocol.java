package com.github.ynfeng.commander.cluster.discovery.impl;

import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public class MulticastDiscoveryProtocol implements NodeDiscoveryProtocol {
    private final CmderLogger logger = CmderLoggerFactory.getSystemLogger();
    private final MulticastDiscoveryConfig config;
    private final AtomicBoolean isStart = new AtomicBoolean();

    public MulticastDiscoveryProtocol(MulticastDiscoveryConfig config) {
        this.config = config;
    }

    @Override
    public void start() {
        if (isStart.compareAndSet(false, true)) {
            System.out.println();
        }
    }

    @Override
    public void shutdown() {
        if (isStart.compareAndSet(true, false)) {
            System.out.println();
        }
    }

    @Override
    public boolean isStarted() {
        return isStart.get();
    }
}
