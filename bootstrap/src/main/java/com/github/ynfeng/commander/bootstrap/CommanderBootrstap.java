package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.ClusterProviderLoader;
import com.github.ynfeng.commander.cluster.Environment;

public class CommanderBootrstap {
    private final ClusterProvider clusterProvider;
    private final StartSteps startSteps = new StartSteps();
    private ShutdownSteps shutdownSteps = new ShutdownSteps();
    private Cluster cluster;

    public CommanderBootrstap(ClusterProviderLoader clusterProviderLoader) {
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
        Environment env = clusterProvider.parepareEnvironment();
        cluster = clusterProvider.getCluster(env);
        return () -> {
        };
    }

    private static ClusterProvider getProviderOrThrowException(ClusterProviderLoader clusterProviderLoader) {
        return clusterProviderLoader.load()
            .orElseThrow(() -> new BootstrapException("No cluster provider found."));
    }
}
