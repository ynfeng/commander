package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.Environment;

public class AtomixClusterProvider implements ClusterProvider {

    @Override
    public Cluster getCluster(Environment env) {
        return new AtomixCluster(env);
    }

    @Override
    public Environment parepareEnvironment() {
        return null;
    }
}
