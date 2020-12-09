package com.github.ynfeng.commander.cluster.discovery;

import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public class MulticastDiscoveryProtocol implements NodeDiscoveryProtocol {
    public static final Type TYPE = new Type();
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

    public static class Type
        implements NodeDiscoveryProtocolType<MulticastDiscoveryConfig, MulticastDiscoveryProtocol> {

        @Override
        public MulticastDiscoveryProtocol newProtocol(MulticastDiscoveryConfig config) {
            return new MulticastDiscoveryProtocol(config);
        }
    }
}
