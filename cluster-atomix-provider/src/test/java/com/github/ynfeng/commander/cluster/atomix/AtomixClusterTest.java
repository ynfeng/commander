package com.github.ynfeng.commander.cluster.atomix;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterContext;
import com.github.ynfeng.commander.cluster.ConsistentMap;
import com.github.ynfeng.commander.cluster.Partition;
import com.github.ynfeng.commander.cluster.PartitionManager;
import java.util.List;
import org.junit.jupiter.api.Test;

class AtomixClusterTest extends AtomixClusterTestSupport{

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

    @Test
    public void should_create_partition_manager() {
        Cluster cluster = getCluster();

        PartitionManager pm = cluster.createPartitionManager();

        assertThat(pm, notNullValue());
    }

    @Test
    public void should_get_consistent_map() {
        Cluster cluster = getCluster();
        cluster.startup();

        ConsistentMap<String, String> store = cluster.getConsistenMap("test store");
        assertThat(store, instanceOf(AtomixConsistenMap.class));

        cluster.shutdown();
    }

    @Test
    public void should_get_local_leader_partitions() {
        Cluster cluster = getCluster();
        cluster.startup();
        PartitionManager pm = cluster.createPartitionManager();
        List<Partition> localLeaderPartitions = pm.getLocalLeaderPartitions();

        assertThat(localLeaderPartitions.size(), is(7));
        cluster.shutdown();
    }

}
