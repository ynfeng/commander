package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.ClusterController;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;

public class AtomixClusterProvider implements ClusterProvider {
    @Override
    public ClusterController createClusterController(ClusterConfig clusterConfig, NodeConfig nodeConfig) {
        return new AtomixClusterController(clusterConfig, nodeConfig);
    }
}
