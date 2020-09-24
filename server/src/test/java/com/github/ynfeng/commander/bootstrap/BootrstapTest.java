package com.github.ynfeng.commander.bootstrap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.ClusterProvider;
import com.github.ynfeng.commander.cluster.ClusterProviderLoader;
import com.github.ynfeng.commander.support.env.Environment;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BootrstapTest {

    private ClusterProviderLoader clusterProviderLoader;

    @Test
    public void should_throw_exception_when_no_cluster_provider() {
        clusterProviderLoader = Mockito.mock(ClusterProviderLoader.class);
        try {
            CommanderServer cluster = new CommanderServer(clusterProviderLoader);
            cluster.bootstrap();
            fail("Should throw exception");
        } catch (Exception e) {
            assertThat(e, instanceOf(ServerException.class));
            assertThat(e.getMessage(), is("No cluster provider found."));
        }
    }

    @Test
    public void should_bootstrap_and_shutdown_cluster() throws Exception {
        clusterProviderLoader = Mockito.mock(ClusterProviderLoader.class);
        Cluster cluster = Mockito.mock(Cluster.class);
        ClusterProvider clusterProvider = Mockito.mock(ClusterProvider.class);
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(clusterProvider.parepareEnvironment()).thenReturn(env);
        Mockito.when(clusterProvider.getCluster(env)).thenReturn(cluster);
        Mockito.when(clusterProviderLoader.load()).thenReturn(Optional.of(clusterProvider));
        CommanderServer clusterBootstrap = new CommanderServer(clusterProviderLoader);
        clusterBootstrap.bootstrap();
        clusterBootstrap.shutdown();

        Mockito.verify(cluster).startup();
        Mockito.verify(cluster).shutdown();
    }
}
