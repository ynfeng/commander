package com.github.ynfeng.commander.bootstrap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.ynfeng.commander.cluster.ClusterProviderLoader;
import com.github.ynfeng.commander.cluster.SPIClusterProviderLoader;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class StandaloneClusterTest {

    private SPIClusterProviderLoader clusterProviderLoader;

    @BeforeEach
    public void setup() {
        clusterProviderLoader = new SPIClusterProviderLoader();
    }

    @Test
    public void should_throw_exception_when_no_cluster_provider() {
        ClusterConfig clusterConfig = Mockito.mock(ClusterConfig.class);
        NodeConfig nodeConfig = Mockito.mock(NodeConfig.class);
        ClusterProviderLoader clusterProviderLoader = Mockito.mock(ClusterProviderLoader.class);
        StandaloneCluster cluster = new StandaloneCluster(clusterConfig, nodeConfig, clusterProviderLoader);

        try {
            cluster.bootstrap();
            fail("Should throw exception");
        } catch (Exception e) {
            assertThat(e, instanceOf(ClusterBootstrapException.class));
            assertThat(e.getMessage(), is("No cluster provider found."));
        }
    }

    @Test
    public void should_bootstrap_standalong_cluster() {
        ClusterConfig clusterConfig = Mockito.mock(ClusterConfig.class);
        NodeConfig nodeConfig = Mockito.mock(NodeConfig.class);
        StandaloneCluster cluster = new StandaloneCluster(clusterConfig, nodeConfig, clusterProviderLoader);
        cluster.bootstrap();
    }
}
