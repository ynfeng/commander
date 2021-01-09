package com.github.ynfeng.commander.cluster.discovery.impl;

import com.github.ynfeng.commander.cluster.ClusterNode;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryConfig;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Host;

public class MulticastDiscoveryConfig implements NodeDiscoveryConfig {

    @Override
    public NodeDiscoveryProtocol.Type protocolType() {
        return () -> new MulticastDiscoveryProtocol(this);
    }

    public Address groupAddress() {
        return null;
    }

    public ClusterNode localNode() {
        return null;
    }

    public Host localHost() {
        return null;
    }

    public long broadcastInterval() {
        return 0;
    }
}
