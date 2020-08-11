package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.ClusterProviderLoader;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;

public class Bootrstap {
    private final ClusterConfig clusterConfig;
    private final NodeConfig nodeConfig;
    private final ClusterProvider clusterProvider;
    private final StartSteps startSteps = new StartSteps();
    private ShutdownSteps shutdownSteps = new ShutdownSteps();
    private Cluster cluster;

    public Bootrstap(ClusterConfig clusterConfig,
                     NodeConfig nodeConfig,
                     ClusterProviderLoader clusterProviderLoader) {
        this.clusterConfig = clusterConfig;
        this.nodeConfig = nodeConfig;
        clusterProvider = getProviderOrThrowException(clusterProviderLoader);
    }

    public void bootstrap() throws Exception {
        startSteps.add(new StartStep("Cluster protocol", this::initClusterProtocol));
        startSteps.add(new StartStep("Cluster services", this::bootClusterServices));
        shutdownSteps = startSteps.execute();
    }

    private AutoCloseable bootClusterServices() {
        cluster.startup();
        return cluster::shutdown;
    }

    public void shutdown() throws Exception {
        shutdownSteps.execute();
    }

    private AutoCloseable initClusterProtocol() {
        cluster = clusterProvider.cluster(clusterConfig, nodeConfig);
        return () -> {
        };
    }

    private static ClusterProvider getProviderOrThrowException(ClusterProviderLoader clusterProviderLoader) {
        return clusterProviderLoader.load()
            .orElseThrow(() -> new BootstrapException("No cluster provider found."));
    }
}
