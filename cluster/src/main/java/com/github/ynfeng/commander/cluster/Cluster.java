package com.github.ynfeng.commander.cluster;

import com.github.ynfeng.commander.primitive.PrimitiveFactory;

public interface Cluster {
    void startup();

    void shutdown();

    PrimitiveFactory getGetPrimitiveFactory();

    ClusterContext getContext();
}
