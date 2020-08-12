package com.github.ynfeng.commander.cluster.atomix;

@SuppressWarnings("checkstyle:LineLength")
public final class ConfigKey {
    public static final String CLUSTER_MGR_PARTITIONS = "cluster.mgr.partitions";
    public static final String CLUSTER_MGR_DATA_DIR = "cluster.mgr.dataDir";
    public static final String CLUSTER_MGR_GROUP_MEMBERS = "cluster.mgr.groupMembers";
    public static final String CLUSTER_BOOTSTRAP_DISCOVERY_BROADCAST_INTERVAL_SECONDS = "cluster.bootstrapDiscoveryBroadcastIntervalSeconds";
    public static final String CLUSTER_MEMBERSHIP_FAILURE_TIME_OUT_SECONDS = "cluster.membership.failureTimeOutSeconds";
    public static final String CLUSTER_MEMBERSHIP_GOSSIP_INTERVAL_SECONDS = "cluster.membership.gossipIntervalSeconds";
    public static final String CLUSTER_MEMBERSHIP_PROBE_INTERVAL_SECONDS = "cluster.membership.probeIntervalSeconds";
    public static final String CLUSTER_MEMBERSHIP_BROADCAST_DISPUTES = "cluster.membership.broadcastDisputes";
    public static final String CLUSTER_MEMBERSHIP_BROADCAST_UPDATES = "cluster.membership.broadcastUpdates";
    public static final String CLUSTER_MEMBERSHIP_GOOSIP_FANOUT = "cluster.membership.goosipFanout";
    public static final String CLUSTER_MEMBERSHIP_NOTIFY_SUSPECT = "cluster.membership.notifySuspect";
    public static final String CLUSTER_MEMBERSHIP_SUSPECT_PROBES = "cluster.membership.suspectProbes";

    private ConfigKey() {
    }
}
