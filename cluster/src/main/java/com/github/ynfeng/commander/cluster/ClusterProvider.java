package com.github.ynfeng.commander.cluster;

import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;

public interface ClusterProvider {
    ClusterController createClusterController(ClusterConfig clusterConfig, NodeConfig nodeConfig);
}
