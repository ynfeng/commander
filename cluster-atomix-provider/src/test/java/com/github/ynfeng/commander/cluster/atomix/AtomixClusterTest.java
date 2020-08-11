package com.github.ynfeng.commander.cluster.atomix;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.SPIClusterProviderLoader;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;
import com.google.common.io.Resources;
import io.atomix.core.Atomix;
import io.atomix.utils.config.ConfigurationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AtomixClusterTest {
    private SPIClusterProviderLoader clusterProvider;

    @BeforeEach
    public void setup() {
        clusterProvider = new SPIClusterProviderLoader();
    }

    @Test
    public void should_startup_and_shutdown_atomix_cluster() {
//        try {
//            String BUILD = Resources.toString(checkNotNull(Atomix.class.getClassLoader().getResource("VERSION"),
//                "VERSION resource is null"), StandardCharsets.UTF_8);
//            Files.write(Paths.get("/home/runner/work/commander/commander/tt"), BUILD.getBytes());
//        } catch (IOException | NullPointerException e) {
//            throw new ConfigurationException("Failed to load Atomix version", e);
//        }
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

        Optional<ClusterProvider> clusterCandicate = clusterProvider.load();

        assertThat(clusterCandicate.isPresent(), is(true));
        ClusterProvider clusterProvider = clusterCandicate.get();
        Cluster cluster = clusterProvider.cluster(clusterConfig, nodeConfig);
        cluster.startup();
        cluster.shutdown();
    }


}
