package com.github.ynfeng.commander.cluster;

import com.github.ynfeng.commander.cluster.primitive.PrimitiveFactory;

public interface Cluster {
    void startup();

    void shutdown();

    PartitionManager createPartitionManager();

    PrimitiveFactory getGetPrimitiveFactory();

    ClusterContext getContext();
}
