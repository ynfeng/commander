package com.github.ynfeng.commander.cluster.config;

public interface ClusterConfig {
    String clusterId();

    long bootstrapDiscoveryBroadcastIntervalSeconds();

    int managementPartitions();

    String[] managementGroupMembers();

    String dataDir();
}
