package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.ClusterProviderLoader;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;
import com.github.ynfeng.commander.server.Address;
import com.github.ynfeng.commander.server.Server;

public class Bootrstap {
    private final ClusterConfig clusterConfig;
    private final NodeConfig nodeConfig;
    private final ClusterProvider clusterProvider;
    private Cluster cluster;
    private Server server;

    public Bootrstap(ClusterConfig clusterConfig,
                     NodeConfig nodeConfig,
                     ClusterProviderLoader clusterProviderLoader) {
        this.clusterConfig = clusterConfig;
        this.nodeConfig = nodeConfig;
        clusterProvider = getProviderOrThrowException(clusterProviderLoader);
    }

    public void bootstrap() throws Exception {
        server = Server.builder()
            .withName(nodeConfig.nodeId())
            .withAddress(Address.of(nodeConfig.address(), nodeConfig.port()))
            .withStartStep("Initialize cluster", this::initCluster)
            .withStartStep("Bootstrap cluster", this::bootCluster)
            .build();
        server.startup();
    }

    private AutoCloseable bootCluster() {
        cluster.startup();
        return cluster::shutdown;
    }

    public void shutdown() throws Exception {
        server.shutdown();
    }

    private AutoCloseable initCluster() {
        cluster = clusterProvider.cluster(clusterConfig, nodeConfig);
        return () -> {
        };
    }


    private static ClusterProvider getProviderOrThrowException(ClusterProviderLoader clusterProviderLoader) {
        return clusterProviderLoader.load()
            .orElseThrow(() -> new BootstrapException("No cluster provider found."));
    }
}
