package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.cluster.ClusterController;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.ClusterProviderLoader;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;
import com.github.ynfeng.commander.server.Address;
import com.github.ynfeng.commander.server.Role;
import com.github.ynfeng.commander.server.Server;

public class StandaloneCluster {
    private final ClusterConfig clusterConfig;
    private final NodeConfig nodeConfig;
    private ClusterProviderLoader clusterProviderLoader;
    private ClusterProvider clusterProvider;

    public StandaloneCluster(ClusterConfig clusterConfig,
                             NodeConfig nodeConfig,
                             ClusterProviderLoader clusterProviderLoader) {
        this.clusterConfig = clusterConfig;
        this.nodeConfig = nodeConfig;
        this.clusterProviderLoader = clusterProviderLoader;
    }

    public void bootstrap() {
        clusterProvider = getProviderOrThrowException();
        Server.builder()
            .withName(nodeConfig.nodeId())
            .withAddress(Address.of(nodeConfig.address(), nodeConfig.port()))
            .withRole(Role.valueOf(nodeConfig.role()))
            .withStartStep("cluster controller", this::startClusterController)
            .build()
            .startup();
    }

    public AutoCloseable startClusterController() {
        ClusterController controller
            = clusterProvider.createClusterController(clusterConfig, nodeConfig);
        controller.startup();
        return () -> controller.shutdown();
    }

    private ClusterProvider getProviderOrThrowException() {
        return clusterProviderLoader.load()
            .orElseThrow(() -> new ClusterBootstrapException("No cluster provider found."));
    }
}
