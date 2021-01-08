package com.github.ynfeng.commander.cluster.discovery;

import com.github.ynfeng.commander.support.Manageable;

public interface NodeDiscoveryProtocol extends Manageable {

    @FunctionalInterface
    interface Type {
        NodeDiscoveryProtocol newProtocol();
    }
}
