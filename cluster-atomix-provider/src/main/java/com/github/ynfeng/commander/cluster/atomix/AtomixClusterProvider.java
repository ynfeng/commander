package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;

public class AtomixClusterProvider implements ClusterProvider {
    @Override
    public Cluster createCluster(ClusterConfig clusterConfig, NodeConfig nodeConfig) {
        return new AtomixCluster(clusterConfig, nodeConfig);
    }
}
