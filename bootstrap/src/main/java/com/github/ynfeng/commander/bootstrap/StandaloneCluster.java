package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.cluster.Cluster;
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
    private final ClusterProvider clusterProvider;
    private Cluster cluster;
    private Server server;

    public StandaloneCluster(ClusterConfig clusterConfig,
                             NodeConfig nodeConfig,
                             ClusterProviderLoader clusterProviderLoader) {
        this.clusterConfig = clusterConfig;
        this.nodeConfig = nodeConfig;
        clusterProvider = getProviderOrThrowException(clusterProviderLoader);
    }

    public void bootstrap() {
        server = Server.builder()
            .withName(nodeConfig.nodeId())
            .withAddress(Address.of(nodeConfig.address(), nodeConfig.port()))
            .withRole(Role.valueOf(nodeConfig.role()))
            .withStartStep("Initialize cluster", this::initCluster)
            .build();
        server.startup();
    }

    public void shutdown() {
        server.shutdown();
    }

    private AutoCloseable initCluster() {
        cluster = clusterProvider.createCluster(clusterConfig, nodeConfig);
        return () -> {
        };
    }


    private ClusterProvider getProviderOrThrowException(ClusterProviderLoader clusterProviderLoader) {
        return clusterProviderLoader.load()
            .orElseThrow(() -> new ClusterBootstrapException("No cluster provider found."));
    }
}
