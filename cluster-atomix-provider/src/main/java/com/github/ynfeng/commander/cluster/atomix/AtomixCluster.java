package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.AbstractCluster;
import com.github.ynfeng.commander.cluster.Environment;
import com.google.common.collect.Lists;
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
import java.util.List;

public class AtomixCluster extends AbstractCluster {
    private static final List<String> EMPTY_LIST = Lists.newArrayList();
    private final Environment env;
    private Atomix atomix;

    public AtomixCluster(Environment environment) {
        env = environment;
        initAtomix();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private void initAtomix() {
        int broadcastIntervalSeconds = env.getProperty(PropertyKey.CLUSTER_DISCOVERY_BROADCAST_INTERVAL_SECONDS, 1);
        String clusterId = env.getProperty(PropertyKey.CLUSTER_ID, null);
        String nodeId = env.getProperty(PropertyKey.CLUSTER_NODE_ID, null);
        String addressId = env.getProperty(PropertyKey.CLUSTER_NODE_ADDRESS, null);
        int port = env.getProperty(PropertyKey.CLUSTER_NODE_PORT, 0);
        atomix = Atomix.builder()
            .withClusterId(clusterId)
            .withMemberId(MemberId.from(nodeId))
            .withAddress(Address.from(addressId, port))
            .withMulticastEnabled()
            .withManagementGroup(buildManagementGroup())
            .withMembershipProtocol(buildMembershipProtocol())
            .withPartitionGroups(buildRaftPartition())
            .setBroadcastInterval(Duration.ofSeconds(Long.valueOf(broadcastIntervalSeconds)))
            .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private RaftPartitionGroup buildManagementGroup() {
        List<String> memberList = env.getProperty(PropertyKey.CLUSTER_MGR_GROUP_MEMBERS, EMPTY_LIST);
        return RaftPartitionGroup
            .builder("system")
            .withNumPartitions(
                env.getProperty(PropertyKey.CLUSTER_MGR_PARTITIONS, 1))
            .withStorageLevel(
                StorageLevel.MAPPED)
            .withDataDirectory(
                new File(env.getProperty(PropertyKey.CLUSTER_MGR_DATA_DIR, "./commander-mgr-data")))
            .withMembers(
                memberList.stream().map(MemberId::new).toArray(MemberId[]::new))
            .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private GroupMembershipProtocol buildMembershipProtocol() {
        int failureTimeoutSeconds = env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_FAILURE_TIME_OUT_SECONDS, 10);
        int gossipIntervalMs = env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_GOSSIP_INTERVAL_MS, 250);
        int probeIntervalSeconds = env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_PROBE_INTERVAL_SECONDS, 1);
        return SwimMembershipProtocol.builder()
            .withFailureTimeout(
                Duration.ofSeconds(Long.valueOf(failureTimeoutSeconds)))
            .withGossipInterval(
                Duration.ofMillis(Long.valueOf(gossipIntervalMs)))
            .withProbeInterval(
                Duration.ofSeconds(Long.valueOf(probeIntervalSeconds)))
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
        List<String> memberList = env.getProperty(PropertyKey.CLUSTER_RAFT_PARTITION_MEMBERS, EMPTY_LIST);
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
                memberList.stream().map(MemberId::new).toArray(MemberId[]::new))
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
