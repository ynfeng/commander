package com.github.ynfeng.commander.cluster.atomix;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.SPIClusterProviderLoader;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;
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
        ClusterConfig clusterConfig = Mockito.mock(ClusterConfig.class);
        NodeConfig nodeConfig = Mockito.mock(NodeConfig.class);
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_BOOTSTRAP_DISCOVERY_BROADCAST_INTERVAL_SECONDS, 1L)).thenReturn(1L);
        Mockito.when(clusterConfig.clusterId()).thenReturn("testCluster");
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_MGR_PARTITIONS, 1)).thenReturn(1);
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_MGR_DATA_DIR, "./commander-mgr-data")).thenReturn("/tmp/atomix-mgr");
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_MGR_GROUP_MEMBERS, new String[] {})).thenReturn(new String[] {"local"});
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_FAILURE_TIME_OUT_SECONDS, 10L)).thenReturn(1L);
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_GOSSIP_INTERVAL_MS, 250L)).thenReturn(250L);
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_PROBE_INTERVAL_SECONDS, 1L)).thenReturn(1L);
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_BROADCAST_DISPUTES, false)).thenReturn(true);
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_BROADCAST_UPDATES, false)).thenReturn(true);
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_GOOSIP_FANOUT, 2)).thenReturn(1);
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_NOTIFY_SUSPECT, false)).thenReturn(true);
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_SUSPECT_PROBES, 2)).thenReturn(1);
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_RAFT_PARTITION_PARTITIONS, 7)).thenReturn(7);
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_RAFT_PARTITION_MEMBERS, new String[] {})).thenReturn(new String[] {"local"});
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_RAFT_PARTITION_DATA_DIR, "./commander-raft-partition-data")).thenReturn("/tmp/atomix-raft-partition");
        Mockito.when(clusterConfig.getConfig(ConfigKey.CLUSTER_RAFT_PARTITION_PARTITION_SIZE, 0)).thenReturn(1);
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
