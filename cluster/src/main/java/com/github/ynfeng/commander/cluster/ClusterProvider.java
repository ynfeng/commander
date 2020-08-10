package com.github.ynfeng.commander.cluster;


import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;

public interface ClusterProvider {
    Cluster createCluster(ClusterConfig clusterConfig, NodeConfig nodeConfig);
}
