package com.github.ynfeng.commander.cluster.discovery.impl;

import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryConfig;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;

public class MulticastDiscoveryConfig implements NodeDiscoveryConfig {

    @Override
    public NodeDiscoveryProtocol.Type<MulticastDiscoveryConfig> protocolType() {
        return new NodeDiscoveryProtocol.Type<MulticastDiscoveryConfig>() {
            @Override
            public MulticastDiscoveryProtocol newProtocol(MulticastDiscoveryConfig config) {
                return new MulticastDiscoveryProtocol(config);
            }
        };
    }
}
