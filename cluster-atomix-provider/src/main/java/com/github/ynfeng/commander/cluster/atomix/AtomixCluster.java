package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.AbstractCluster;
import com.github.ynfeng.commander.cluster.Environment;
import io.atomix.cluster.MemberId;
import io.atomix.cluster.protocol.GroupMembershipProtocol;
import io.atomix.cluster.protocol.SwimMembershipProtocol;
import io.atomix.core.Atomix;
import io.atomix.primitive.partition.ManagedPartitionGroup;
import io.atomix.protocols.raft.partition.RaftPartitionGroup;
import io.atomix.storage.StorageLevel;
import io.atomix.utils.net.Address;
import java.io.File;
import java.time.Duration;

public class AtomixCluster extends AbstractCluster {
    private static final String[] EMPTY_STRING_ARRAY = {};
    private final Environment env;
    private Atomix atomix;

    public AtomixCluster(Environment environment) {
        env = environment;
        initAtomix();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private void initAtomix() {
        atomix = Atomix.builder()
            .withClusterId(env.getProperty(PropertyKey.CLUSTER_ID, null))
            .withMemberId(MemberId.from(env.getProperty(PropertyKey.CLUSTER_NODE_ID, null)))
            .withAddress(Address.from(
                env.getProperty(PropertyKey.CLUSTER_NODE_ADDRESS, null),
                env.getProperty(PropertyKey.CLUSTER_NODE_PORT, 0)))
            .withMulticastEnabled()
            .withManagementGroup(buildManagementGroup())
            .withMembershipProtocol(buildMembershipProtocol())
            .withPartitionGroups(buildRaftPartition())
            .setBroadcastInterval(
                Duration.ofSeconds(
                    env.getProperty(PropertyKey.CLUSTER_BOOTSTRAP_DISCOVERY_BROADCAST_INTERVAL_SECONDS, 1L)))
            .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private RaftPartitionGroup buildManagementGroup() {
        return RaftPartitionGroup
            .builder("system")
            .withNumPartitions(
                env.getProperty(PropertyKey.CLUSTER_MGR_PARTITIONS, 1))
            .withStorageLevel(
                StorageLevel.MAPPED)
            .withDataDirectory(
                new File(env.getProperty(PropertyKey.CLUSTER_MGR_DATA_DIR, "./commander-mgr-data")))
            .withMembers(
                env.getProperty(PropertyKey.CLUSTER_MGR_GROUP_MEMBERS, EMPTY_STRING_ARRAY))
            .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private GroupMembershipProtocol buildMembershipProtocol() {
        return SwimMembershipProtocol.builder()
            .withFailureTimeout(
                Duration.ofSeconds(
                    env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_FAILURE_TIME_OUT_SECONDS, 10L)))
            .withGossipInterval(
                Duration.ofMillis(
                    env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_GOSSIP_INTERVAL_MS, 250L)))
            .withProbeInterval(
                Duration.ofSeconds(
                    env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_PROBE_INTERVAL_SECONDS, 1L)))
            .withBroadcastDisputes(
                env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_BROADCAST_DISPUTES, false))
            .withBroadcastUpdates(
                env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_BROADCAST_UPDATES, false))
            .withGossipFanout(
                env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_GOOSIP_FANOUT, 2))
            .withNotifySuspect(
                env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_NOTIFY_SUSPECT, false))
            .withSuspectProbes(
                env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_SUSPECT_PROBES, 2))
            .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private ManagedPartitionGroup buildRaftPartition() {
        return RaftPartitionGroup
            .builder("raft-partition")
            .withNumPartitions(
                env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_PARTITIONS, 7))
            .withPartitionSize(
                env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_PARTITION_SIZE, 0))
            .withStorageLevel(
                StorageLevel.DISK)
            .withDataDirectory(
                new File(env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_DATA_DIR,
                    "./commander-raft-partition-data")))
            .withMembers(
                env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_MEMBERS, EMPTY_STRING_ARRAY))
            .build();
    }

    @Override
    public void start() {
        atomix.start().join();
    }

    @Override
    public void stop() {
        atomix.stop().join();
    }
}
