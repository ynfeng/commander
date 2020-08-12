package com.github.ynfeng.commander.cluster.atomix;

@SuppressWarnings("checkstyle:LineLength")
public final class PropertyKey {
    public static final String CLUSTER_MGR_PARTITIONS = "cluster.mgr.partitions";
    public static final String CLUSTER_MGR_DATA_DIR = "cluster.mgr.dataDir";
    public static final String CLUSTER_MGR_GROUP_MEMBERS = "cluster.mgr.groupMembers";
    public static final String CLUSTER_BOOTSTRAP_DISCOVERY_BROADCAST_INTERVAL_SECONDS = "cluster.bootstrapDiscoveryBroadcastIntervalSeconds";
    public static final String CLUSTER_MEMBERSHIP_FAILURE_TIME_OUT_SECONDS = "cluster.membership.failureTimeOutSeconds";
    public static final String CLUSTER_MEMBERSHIP_GOSSIP_INTERVAL_MS = "cluster.membership.gossipIntervalSeconds";
    public static final String CLUSTER_MEMBERSHIP_PROBE_INTERVAL_SECONDS = "cluster.membership.probeIntervalSeconds";
    public static final String CLUSTER_MEMBERSHIP_BROADCAST_DISPUTES = "cluster.membership.broadcastDisputes";
    public static final String CLUSTER_MEMBERSHIP_BROADCAST_UPDATES = "cluster.membership.broadcastUpdates";
    public static final String CLUSTER_MEMBERSHIP_GOOSIP_FANOUT = "cluster.membership.goosipFanout";
    public static final String CLUSTER_MEMBERSHIP_NOTIFY_SUSPECT = "cluster.membership.notifySuspect";
    public static final String CLUSTER_MEMBERSHIP_SUSPECT_PROBES = "cluster.membership.suspectProbes";
    public static final String CLUSTER_RAFT_PARTITION_PARTITIONS = "cluster.raftPartition.partitions";
    public static final String CLUSTER_RAFT_PARTITION_MEMBERS = "cluster.raftPartition.members";
    public static final String CLUSTER_RAFT_PARTITION_DATA_DIR = "cluster.raftPartition.dataDir";
    public static final String CLUSTER_RAFT_PARTITION_PARTITION_SIZE = "cluster.raftPartition.partitionSize";
    public static final String CLUSTER_ID = "cluster.id";
    public static final String CLUSTER_NODE_ID = "cluster.node.id";
    public static final String CLUSTER_NODE_ADDRESS = "cluster.node.address";
    public static final String CLUSTER_NODE_PORT = "cluster.node.port";

    private PropertyKey() {
    }
}
