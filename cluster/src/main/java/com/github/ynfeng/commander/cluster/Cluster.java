package com.github.ynfeng.commander.cluster;

public interface Cluster {
    void startup();

    void shutdown();

    PartitionManager createPartitionManager();

    ClusterContext getContext();
}
