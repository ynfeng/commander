package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.ClusterController;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;

public class AtomixClusterProvider implements ClusterProvider {
    @Override
    public ClusterController getClusterController(ClusterConfig clusterConfig) {
        return null;
    }
}
