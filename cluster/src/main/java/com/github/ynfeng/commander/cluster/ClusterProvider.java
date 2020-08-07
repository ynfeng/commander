package com.github.ynfeng.commander.cluster;

import com.github.ynfeng.commander.cluster.config.ClusterConfig;

public interface ClusterProvider {
    ClusterController getClusterController(ClusterConfig clusterConfig);
}
