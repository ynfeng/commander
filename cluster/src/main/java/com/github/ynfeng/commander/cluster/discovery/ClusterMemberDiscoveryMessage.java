package com.github.ynfeng.commander.cluster.discovery;

import com.github.ynfeng.commander.cluster.ClusterMember;

public class ClusterMemberDiscoveryMessage {
    private ClusterMember node;
    private Type type;

    @SuppressWarnings("unused")
    private ClusterMemberDiscoveryMessage() {
    }

    private ClusterMemberDiscoveryMessage(ClusterMember node, Type type) {
        this.node = node;
        this.type = type;
    }

    public static ClusterMemberDiscoveryMessage createOnlineMessage(ClusterMember node) {
        return new ClusterMemberDiscoveryMessage(node, Type.ONLINE);
    }

    public static ClusterMemberDiscoveryMessage createOfflineMessage(ClusterMember node) {
        return new ClusterMemberDiscoveryMessage(node, Type.OFFLINE);
    }

    public Type type() {
        return type;
    }

    public ClusterMember node() {
        return node;
    }

    public enum Type {
        ONLINE("clusterNodeOnline"), OFFLINE("clusterNodeOffline");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    @Override
    public String toString() {
        return "NodeDiscoveryMessage{"
            + "node=" + node
            + ", type=" + type
            + '}';
    }
}
