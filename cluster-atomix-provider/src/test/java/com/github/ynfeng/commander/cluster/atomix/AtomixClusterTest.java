package com.github.ynfeng.commander.cluster.atomix;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterContext;
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
}
