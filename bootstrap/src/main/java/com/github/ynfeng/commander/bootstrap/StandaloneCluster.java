package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.cluster.ClusterController;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.ClusterProviderLoader;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;
import com.github.ynfeng.commander.server.Address;
import com.github.ynfeng.commander.server.Role;
import com.github.ynfeng.commander.server.Server;
import java.util.concurrent.CompletableFuture;

public class StandaloneCluster {
    private final ClusterConfig clusterConfig;
    private final NodeConfig nodeConfig;
    private final ClusterProvider clusterProvider;
    private final CompletableFuture<Void> shutdownFuture = new CompletableFuture<>();

    public StandaloneCluster(ClusterConfig clusterConfig,
                             NodeConfig nodeConfig,
                             ClusterProviderLoader clusterProviderLoader) {
        this.clusterConfig = clusterConfig;
        this.nodeConfig = nodeConfig;
        clusterProvider = getProviderOrThrowException(clusterProviderLoader);
    }

    public CompletableFuture<Void> bootstrap() {
        Server.builder()
            .withName(nodeConfig.nodeId())
            .withAddress(Address.of(nodeConfig.address(), nodeConfig.port()))
            .withRole(Role.valueOf(nodeConfig.role()))
            .withStartStep("Cluster controller", this::startClusterController)
            .build()
            .startup();
        return shutdownFuture;
    }

    public AutoCloseable startClusterController() {
        ClusterController controller
            = clusterProvider.createClusterController(clusterConfig, nodeConfig);
        controller.startup();
        return () -> controller.shutdown();
    }

    private ClusterProvider getProviderOrThrowException(ClusterProviderLoader clusterProviderLoader) {
        return clusterProviderLoader.load()
            .orElseThrow(() -> new ClusterBootstrapException("No cluster provider found."));
    }
}
