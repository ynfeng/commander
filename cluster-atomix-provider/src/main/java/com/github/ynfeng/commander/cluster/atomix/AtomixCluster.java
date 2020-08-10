package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;

public class AtomixCluster extends Cluster {
    private final ClusterConfig clusterConfig;
    private final NodeConfig nodeConfig;

    public AtomixCluster(ClusterConfig clusterConfig, NodeConfig nodeConfig) {

        this.clusterConfig = clusterConfig;
        this.nodeConfig = nodeConfig;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
