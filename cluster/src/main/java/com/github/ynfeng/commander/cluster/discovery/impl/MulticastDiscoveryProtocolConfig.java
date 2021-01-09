package com.github.ynfeng.commander.cluster.discovery.impl;

import com.github.ynfeng.commander.cluster.ClusterNode;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocolConfig;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Host;

public class MulticastDiscoveryProtocolConfig implements NodeDiscoveryProtocolConfig {
    private Host localhost;
    private Address groupAddress;
    private ClusterNode localNode;
    private long broadcastInterval = 5;

    @Override
    public NodeDiscoveryProtocol.Type protocolType() {
        return () -> new MulticastDiscoveryProtocol(this);
    }

    public Address groupAddress() {
        return groupAddress;
    }

    public ClusterNode localNode() {
        return localNode;
    }

    public Host localHost() {
        return localhost;
    }

    public long broadcastInterval() {
        return broadcastInterval;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final MulticastDiscoveryProtocolConfig config = new MulticastDiscoveryProtocolConfig();

        public Builder localHost(Host host) {
            config.localhost = host;
            return this;
        }

        public Builder groupAddress(Address address) {
            config.groupAddress = address;
            return this;
        }

        public Builder localNode(ClusterNode node) {
            config.localNode = node;
            return this;
        }

        public Builder broadcastInterval(long interval) {
            config.broadcastInterval = interval;
            return this;
        }

        public MulticastDiscoveryProtocolConfig build() {
            return config;
        }
    }
}
