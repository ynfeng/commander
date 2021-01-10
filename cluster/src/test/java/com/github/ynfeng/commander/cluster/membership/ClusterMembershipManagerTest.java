package com.github.ynfeng.commander.cluster.membership;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.ClusterMember;
import com.github.ynfeng.commander.cluster.discovery.impl.MulticastDiscoveryProtocolConfig;
import com.github.ynfeng.commander.cluster.membership.ClusterMembershipConfig;
import com.github.ynfeng.commander.cluster.membership.ClusterMembershipManager;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Host;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

public class ClusterMembershipManagerTest {

    @Test
    public void should_discovery_cluster_node() {
        MulticastDiscoveryProtocolConfig discoveryProtocolConfig1 = MulticastDiscoveryProtocolConfig.builder()
            .localMember(ClusterMember.of("1"))
            .broadcastInterval(1)
            .groupAddress(Address.of("230.0.0.2", 12344))
            .localHost(Host.of("127.0.0.1"))
            .build();
        MulticastDiscoveryProtocolConfig discoveryProtocolConfig2 = MulticastDiscoveryProtocolConfig.builder()
            .localMember(ClusterMember.of("2"))
            .broadcastInterval(1)
            .groupAddress(Address.of("230.0.0.2", 12344))
            .localHost(Host.of("127.0.0.1"))
            .build();

        ClusterMembershipConfig config1 = ClusterMembershipConfig.builder()
            .memberDiscoveryConfig(discoveryProtocolConfig1)
            .localMember(ClusterMember.of("1"))
            .build();
        ClusterMembershipConfig config2 = ClusterMembershipConfig.builder()
            .memberDiscoveryConfig(discoveryProtocolConfig2)
            .localMember(ClusterMember.of("2"))
            .build();
        ClusterMembershipManager manager1 = new ClusterMembershipManager(config1);
        ClusterMembershipManager manager2 = new ClusterMembershipManager(config2);

        manager1.start();
        manager2.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        assertThat(manager1.clusterMembers(), is(Sets.newHashSet(ClusterMember.of("2"))));
        assertThat(manager2.clusterMembers(), is(Sets.newHashSet(ClusterMember.of("1"))));

        manager1.shutdown();
        manager2.shutdown();
    }
}
