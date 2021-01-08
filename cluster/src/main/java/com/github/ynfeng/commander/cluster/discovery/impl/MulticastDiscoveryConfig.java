package com.github.ynfeng.commander.cluster.discovery.impl;

import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryConfig;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;
import com.github.ynfeng.commander.support.Address;

public class MulticastDiscoveryConfig implements NodeDiscoveryConfig {

    @Override
    public NodeDiscoveryProtocol.Type protocolType() {
        return new NodeDiscoveryProtocol.Type() {
            @Override
            public MulticastDiscoveryProtocol newProtocol() {
                return new MulticastDiscoveryProtocol(MulticastDiscoveryConfig.this);
            }
        };
    }

    public Address localAddress() {
        return null;
    }

    public Address groupAddress() {
        return null;
    }
}
