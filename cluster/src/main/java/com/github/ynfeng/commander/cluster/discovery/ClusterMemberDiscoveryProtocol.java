package com.github.ynfeng.commander.cluster.discovery;

import com.github.ynfeng.commander.cluster.ClusterMember;
import com.github.ynfeng.commander.support.Manageable;
import java.util.function.Consumer;

public interface ClusterMemberDiscoveryProtocol extends Manageable {

    void addClusterNodeChangeListener(ClusterMemberDiscoveryMessage.Type type, Consumer<ClusterMember> listener);

    void broadcastOffline();

    @FunctionalInterface
    interface Type {
        ClusterMemberDiscoveryProtocol newProtocol();
    }
}
