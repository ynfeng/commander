package com.github.ynfeng.commander.cluster.discovery;

import com.github.ynfeng.commander.cluster.ClusterNode;
import java.util.Arrays;

public class NodeDiscoveryMessage {
    private ClusterNode node;
    private Type type;

    @SuppressWarnings("unused")
    private NodeDiscoveryMessage() {
    }

    private NodeDiscoveryMessage(ClusterNode node, Type type) {
        this.node = node;
        this.type = type;
    }

    public static NodeDiscoveryMessage createOnlineMessage(ClusterNode node) {
        return new NodeDiscoveryMessage(node, Type.Online);
    }

    public Type type() {
        return type;
    }

    public ClusterNode node() {
        return node;
    }

    public enum Type {
        Online("clusterNodeOnline"), Offline("clusterNodeOffline");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public static Type ofValue(String value) {
            return Arrays.stream(values())
                .filter(each -> each.value.equals(value))
                .findAny().orElse(null);
        }
    }
}
