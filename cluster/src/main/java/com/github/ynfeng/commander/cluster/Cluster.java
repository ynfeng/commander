package com.github.ynfeng.commander.cluster;

import com.github.ynfeng.commander.cluster.primitive.DistributedMap;

public interface Cluster {
    void startup();

    void shutdown();

    PartitionManager createPartitionManager();

    <K, V> DistributedMap<K, V> getConsistenMap(String name);

    ClusterContext getContext();
}
