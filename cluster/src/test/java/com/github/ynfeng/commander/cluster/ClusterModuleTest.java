package com.github.ynfeng.commander.cluster;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ClusterModuleTest {

    @Test
    void should_get_name() {
        ClusterModule clusterModule = new ClusterModule();
        clusterModule.init();

        assertThat(clusterModule.name(), is("commander-cluster-module"));
    }
}
