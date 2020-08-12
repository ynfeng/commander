package com.github.ynfeng.commander.cluster.atomix;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.Environment;
import com.github.ynfeng.commander.cluster.SPIClusterProviderLoader;
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
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_BOOTSTRAP_DISCOVERY_BROADCAST_INTERVAL_SECONDS, 1L)).thenReturn(1L);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_ID, null)).thenReturn("testCluster");
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MGR_PARTITIONS, 1)).thenReturn(1);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MGR_DATA_DIR, "./commander-mgr-data")).thenReturn("/tmp/atomix-mgr");
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MGR_GROUP_MEMBERS, new String[] {})).thenReturn(new String[] {"local"});
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_FAILURE_TIME_OUT_SECONDS, 10L)).thenReturn(1L);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_GOSSIP_INTERVAL_MS, 250L)).thenReturn(250L);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_PROBE_INTERVAL_SECONDS, 1L)).thenReturn(1L);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_BROADCAST_DISPUTES, false)).thenReturn(true);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_BROADCAST_UPDATES, false)).thenReturn(true);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_GOOSIP_FANOUT, 2)).thenReturn(1);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_NOTIFY_SUSPECT, false)).thenReturn(true);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_SUSPECT_PROBES, 2)).thenReturn(1);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_PARTITIONS, 7)).thenReturn(7);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_MEMBERS, new String[] {})).thenReturn(new String[] {"local"});
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_DATA_DIR, "./commander-raft-partition-data")).thenReturn("/tmp/atomix-raft-partition");
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_PARTITION_SIZE, 0)).thenReturn(1);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_NODE_ADDRESS, null)).thenReturn("127.0.0.1");
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_NODE_PORT, 0)).thenReturn(8098);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_NODE_ID, null)).thenReturn("local");
        Optional<ClusterProvider> clusterCandicate = clusterProvider.load();

        assertThat(clusterCandicate.isPresent(), is(true));
        ClusterProvider clusterProvider = Mockito.spy(clusterCandicate.get());
        Mockito.when(clusterProvider.parepareEnvironment()).thenReturn(env);
        Cluster cluster = clusterProvider.getCluster(clusterProvider.parepareEnvironment());
        cluster.startup();
        cluster.shutdown();
    }
}
