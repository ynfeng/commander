package com.github.ynfeng.commander.cluster.atomix;

@SuppressWarnings("checkstyle:LineLength")
public final class ConfigKey {
    public static final String CLUSTER_MGR_PARTITIONS = "cluster.mgr.partitions";
    public static final String CLUSTER_MGR_DATA_DIR = "cluster.mgr.dataDir";
    public static final String CLUSTER_MGR_GROUP_MEMBERS = "cluster.mgr.groupMembers";
    public static final String CLUSTER_BOOTSTRAP_DISCOVERY_BROADCAST_INTERVAL_SECONDS = "cluster.bootstrapDiscoveryBroadcastIntervalSeconds";

    private ConfigKey() {
    }
}
