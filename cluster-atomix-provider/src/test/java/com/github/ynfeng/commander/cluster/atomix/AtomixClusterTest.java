package com.github.ynfeng.commander.cluster.atomix;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterContext;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.Environment;
import com.github.ynfeng.commander.cluster.SPIClusterProviderLoader;
import com.google.common.collect.Lists;
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
        Cluster cluster = getCluster();

        cluster.startup();
        cluster.shutdown();
    }

    @Test
    public void should_get_cluster_context_from_atomix_cluster() {
        Cluster cluster = getCluster();
        cluster.startup();

        ClusterContext context = cluster.getContext();
        cluster.shutdown();

        assertThat(context, notNullValue());
    }

    private Cluster getCluster() {
        Environment env = mockEnvironment();
        Optional<ClusterProvider> clusterCandicate = clusterProvider.load();
        assertThat(clusterCandicate.isPresent(), is(true));
        ClusterProvider clusterProvider = Mockito.spy(clusterCandicate.get());
        Mockito.when(clusterProvider.parepareEnvironment()).thenReturn(env);
        return clusterProvider.getCluster(clusterProvider.parepareEnvironment());
    }

    private Environment mockEnvironment() {
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_DISCOVERY_BROADCAST_INTERVAL_SECONDS, 1)).thenReturn(1);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_ID)).thenReturn("testCluster");
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MGR_PARTITIONS, 1)).thenReturn(1);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MGR_DATA_DIR, "./commander-mgr-data")).thenReturn("/tmp/atomix-mgr");
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MGR_GROUP_MEMBERS, Lists.newArrayList())).thenReturn(Lists.newArrayList("local"));
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_FAILURE_TIME_OUT_SECONDS, 10)).thenReturn(1);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_GOSSIP_INTERVAL_MS, 250)).thenReturn(250);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_PROBE_INTERVAL_SECONDS, 1)).thenReturn(1);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_BROADCAST_DISPUTES, false)).thenReturn(true);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_BROADCAST_UPDATES, false)).thenReturn(true);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_GOOSIP_FANOUT, 2)).thenReturn(1);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_NOTIFY_SUSPECT, false)).thenReturn(true);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_SUSPECT_PROBES, 2)).thenReturn(1);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_PARTITIONS, 7)).thenReturn(7);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_MEMBERS, Lists.newArrayList())).thenReturn(Lists.newArrayList("local"));
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_DATA_DIR, "./commander-raft-partition-data")).thenReturn("/tmp/atomix-raft-partition");
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_PARTITION_SIZE, 0)).thenReturn(1);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_NODE_ADDRESS)).thenReturn("127.0.0.1");
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_NODE_PORT, 0)).thenReturn(8098);
        Mockito.when(env.getProperty(PropertyKey.CLUSTER_NODE_ID)).thenReturn("local");
        return env;
    }
}
