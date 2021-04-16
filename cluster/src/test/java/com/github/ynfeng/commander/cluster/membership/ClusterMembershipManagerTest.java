package com.github.ynfeng.commander.cluster.membership;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;

import com.github.ynfeng.commander.cluster.ClusterMember;
import com.github.ynfeng.commander.cluster.discovery.impl.MulticastDiscoveryProtocolConfig;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Host;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

class ClusterMembershipManagerTest {

    @Test
    void should_discovery_cluster_node() {
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
            .localMember(discoveryProtocolConfig1.localMember())
            .build();
        ClusterMembershipConfig config2 = ClusterMembershipConfig.builder()
            .memberDiscoveryConfig(discoveryProtocolConfig2)
            .localMember(discoveryProtocolConfig2.localMember())
            .build();
        ClusterMembershipManager manager1 = new ClusterMembershipManager(config1);
        ClusterMembershipManager manager2 = new ClusterMembershipManager(config2);

        manager1.start();
        manager2.start();

        await().until(() -> manager1.clusterMembers(), is(Sets.newHashSet(ClusterMember.of("2"))));
        await().until(() -> manager2.clusterMembers(), is(Sets.newHashSet(ClusterMember.of("1"))));

        manager1.shutdown();
        manager2.shutdown();
    }
}
