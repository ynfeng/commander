package com.github.ynfeng.commander.cluster.atomix;


import com.github.ynfeng.commander.cluster.AbstractClusterController;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;

public class AtomixClusterController extends AbstractClusterController {
    private final ClusterConfig clusterConfig;
    private final NodeConfig nodeConfig;

    public AtomixClusterController(ClusterConfig clusterConfig, NodeConfig nodeConfig) {
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
