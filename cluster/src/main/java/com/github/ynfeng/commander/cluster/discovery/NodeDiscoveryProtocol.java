package com.github.ynfeng.commander.cluster.discovery;

import com.github.ynfeng.commander.cluster.ClusterNode;
import com.github.ynfeng.commander.support.Manageable;
import java.util.function.Consumer;

public interface NodeDiscoveryProtocol extends Manageable {

    void addListener(NodeDiscoveryMessage.Type type, Consumer<ClusterNode> listener);

    void broadcastOffline();

    @FunctionalInterface
    interface Type {
        NodeDiscoveryProtocol newProtocol();
    }
}
