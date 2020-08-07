package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.cluster.ClusterController;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.ClusterProviderLoader;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;

public class StandaloneCluster {
    private final ClusterConfig clusterConfig;
    private final NodeConfig nodeConfig;
    private ClusterProviderLoader clusterProviderLoader;

    public StandaloneCluster(ClusterConfig clusterConfig,
                             NodeConfig nodeConfig,
                             ClusterProviderLoader clusterProviderLoader) {
        this.clusterConfig = clusterConfig;
        this.nodeConfig = nodeConfig;
        this.clusterProviderLoader = clusterProviderLoader;
    }

    public void bootstrap() {
        ClusterProvider clusterProvider = getProviderOrThrowException();
        ClusterController clusterController = clusterProvider.getClusterController(clusterConfig);
    }

    private ClusterProvider getProviderOrThrowException() {
        return clusterProviderLoader.load()
            .orElseThrow(() -> new ClusterBootstrapException("No cluster provider found."));
    }
}
