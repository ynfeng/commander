package com.github.ynfeng.commander.cluster.discovery;

import com.github.ynfeng.commander.support.Config;

@FunctionalInterface
public interface NodeDiscoveryConfig extends Config {
    NodeDiscoveryProtocol.Type protocolType();
}
