package com.github.ynfeng.commander.cluster;

public interface Cluster {
    void startup();

    void shutdown();

    PartitionManager createPartitionManager();

    <K, V> ConsistentMap<K, V> getConsistenMap(String name);

    ClusterContext getContext();
}
