package com.github.ynfeng.commander.cluster.discovery;

import com.github.ynfeng.commander.support.ProtocolType;

public interface NodeDiscoveryProtocol {

    @FunctionalInterface
    interface NodeDiscoveryProtocolType<C extends NodeDiscoveryConfig, T extends NodeDiscoveryProtocol>
        extends ProtocolType<C, T> {
    }
}
