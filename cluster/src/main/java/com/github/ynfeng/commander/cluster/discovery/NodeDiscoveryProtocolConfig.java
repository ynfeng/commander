package com.github.ynfeng.commander.cluster.discovery;

import com.github.ynfeng.commander.support.Config;

@FunctionalInterface
public interface NodeDiscoveryProtocolConfig extends Config {
    NodeDiscoveryProtocol.Type protocolType();
}
