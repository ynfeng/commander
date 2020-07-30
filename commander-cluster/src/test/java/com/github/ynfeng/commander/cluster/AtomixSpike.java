package com.github.ynfeng.commander.cluster;

import io.atomix.cluster.Node;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.core.Atomix;
import io.atomix.core.map.AtomicMap;
import io.atomix.protocols.raft.MultiRaftProtocol;
import io.atomix.protocols.raft.ReadConsistency;
import io.atomix.protocols.raft.partition.RaftPartitionGroup;
import io.atomix.utils.net.Address;
import java.io.File;

public class AtomixSpike {


    public static final void main(String[] args) {
        Atomix atomix = Atomix.builder()
            .withMemberId("member2")
            .withClusterId("my_cluster")
            .withAddress(Address.local())
            .withMembershipProvider(BootstrapDiscoveryProvider.builder()
                .withNodes(
                    Node.builder()
                        .withId("member1")
                        .withAddress(Address.from(1234))
                        .build()
                )
                .build()
            ).withManagementGroup(
                RaftPartitionGroup.builder("system")
                    .withNumPartitions(1)
                    .withDataDirectory(new File("/tmp/manager"))
                    .withMembers("member1")
                    .build())
            .withPartitionGroups(
                RaftPartitionGroup.builder("data")
                    .withNumPartitions(2)
                    .withDataDirectory(new File("/tmp/data"))
                    .build()
            )
            .build();
        atomix.start().join();
        MultiRaftProtocol protocol = MultiRaftProtocol.builder()
            .withReadConsistency(ReadConsistency.LINEARIZABLE)
            .build();

        AtomicMap<String, String> map = atomix.<String, String>atomicMapBuilder("my-map")
            .withProtocol(protocol)
            .build();
        map.put("ss", "ss");
        System.out.println(map.get("ss"));
    }
}
