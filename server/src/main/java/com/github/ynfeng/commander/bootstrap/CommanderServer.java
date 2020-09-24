package com.github.ynfeng.commander.bootstrap;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.ClusterProviderLoader;
import com.github.ynfeng.commander.support.env.Environment;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;

public class CommanderServer {
    private static final CmderLogger LOG = CmderLoggerFactory.getSystemLogger();
    private final ClusterProvider clusterProvider;
    private final StartSteps startSteps = new StartSteps();
    private ShutdownSteps shutdownSteps = new ShutdownSteps();
    private Cluster cluster;

    public CommanderServer(ClusterProviderLoader clusterProviderLoader) {
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
        LOG.debug("Prepared {} cluster environment.", env.name());
        cluster = clusterProvider.getCluster(env);
        return () -> {
        };
    }

    private static ClusterProvider getProviderOrThrowException(ClusterProviderLoader clusterProviderLoader) {
        return clusterProviderLoader.load()
            .orElseThrow(() -> new ServerException("No cluster provider found."));
    }
}
