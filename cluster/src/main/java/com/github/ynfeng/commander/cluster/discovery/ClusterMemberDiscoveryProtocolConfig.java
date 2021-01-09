package com.github.ynfeng.commander.cluster.discovery;

import com.github.ynfeng.commander.support.Config;

@FunctionalInterface
public interface ClusterMemberDiscoveryProtocolConfig extends Config {
    ClusterMemberDiscoveryProtocol.Type protocolType();
}
