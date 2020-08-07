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
        Mockito.when(nodeConfig.address()).thenReturn("127.0.0.1");
        Mockito.when(nodeConfig.port()).thenReturn(1234);
        Mockito.when(nodeConfig.nodeId()).thenReturn("local");
        Mockito.when(nodeConfig.role()).thenReturn("Controller");

        StandaloneCluster cluster = new StandaloneCluster(clusterConfig, nodeConfig, clusterProviderLoader);
        cluster.bootstrap();
    }
}
