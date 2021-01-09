package com.github.ynfeng.commander.cluster.discovery.impl;

import com.github.ynfeng.commander.cluster.ClusterMember;
import com.github.ynfeng.commander.cluster.discovery.ClusterMemberDiscoveryProtocol;
import com.github.ynfeng.commander.cluster.discovery.ClusterMemberDiscoveryProtocolConfig;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Host;

public class MulticastDiscoveryProtocolConfig implements ClusterMemberDiscoveryProtocolConfig {
    private Host localhost;
    private Address groupAddress;
    private ClusterMember localMember;
    private long broadcastInterval = 5;

    @Override
    public ClusterMemberDiscoveryProtocol.Type protocolType() {
        return () -> new MulticastDiscoveryProtocol(this);
    }

    public Address groupAddress() {
        return groupAddress;
    }

    public ClusterMember localMember() {
        return localMember;
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

        public Builder localMember(ClusterMember member) {
            config.localMember = member;
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
