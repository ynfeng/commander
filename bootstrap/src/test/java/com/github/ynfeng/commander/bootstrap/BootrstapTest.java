package com.github.ynfeng.commander.bootstrap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.ClusterProviderLoader;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BootrstapTest {

    private ClusterProviderLoader clusterProviderLoader;

    @Test
    public void should_throw_exception_when_no_cluster_provider() {
        ClusterConfig clusterConfig = Mockito.mock(ClusterConfig.class);
        NodeConfig nodeConfig = Mockito.mock(NodeConfig.class);
        clusterProviderLoader = Mockito.mock(ClusterProviderLoader.class);

        try {
            Bootrstap cluster = new Bootrstap(clusterConfig, nodeConfig, clusterProviderLoader);
            cluster.bootstrap();
            fail("Should throw exception");
        } catch (Exception e) {
            assertThat(e, instanceOf(BootstrapException.class));
            assertThat(e.getMessage(), is("No cluster provider found."));
        }
    }

    @Test
    public void should_bootstrap_cluster() throws Exception {
        clusterProviderLoader = Mockito.mock(ClusterProviderLoader.class);
        Cluster cluster = Mockito.mock(Cluster.class);
        ClusterProvider clusterProvider = Mockito.mock(ClusterProvider.class);
        Mockito.when(clusterProvider.cluster(any(ClusterConfig.class), any(NodeConfig.class)))
            .thenReturn(cluster);
        Mockito.when(clusterProviderLoader.load())
            .thenReturn(Optional.of(clusterProvider));
        ClusterConfig clusterConfig = Mockito.mock(ClusterConfig.class);
        NodeConfig nodeConfig = Mockito.mock(NodeConfig.class);
        Mockito.when(clusterConfig.bootstrapDiscoveryBroadcastIntervalSeconds()).thenReturn(1L);
        Mockito.when(clusterConfig.clusterId()).thenReturn("testCluster");
        Mockito.when(clusterConfig.managementPartitions()).thenReturn(1);
        Mockito.when(clusterConfig.managementDataDir()).thenReturn("/tmp/atomix-mgr");
        Mockito.when(clusterConfig.managementGroupMembers()).thenReturn(new String[] {"local"});
        Mockito.when(nodeConfig.address()).thenReturn("127.0.0.1");
        Mockito.when(nodeConfig.port()).thenReturn(8098);
        Mockito.when(nodeConfig.nodeId()).thenReturn("local");

        Bootrstap clusterBootstrap = new Bootrstap(clusterConfig, nodeConfig, clusterProviderLoader);
        clusterBootstrap.bootstrap();
        clusterBootstrap.shutdown();

        Mockito.verify(cluster).startup();
        Mockito.verify(cluster).shutdown();
    }
}
