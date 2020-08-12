package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.AbstractCluster;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;
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
    private final ClusterConfig clusterConfig;
    private final NodeConfig nodeConfig;
    private Atomix atomix;

    public AtomixCluster(ClusterConfig clusterConfig, NodeConfig nodeConfig) {
        this.clusterConfig = clusterConfig;
        this.nodeConfig = nodeConfig;
        initAtomix();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private void initAtomix() {
        atomix = Atomix.builder()
            .withClusterId(clusterConfig.clusterId())
            .withMemberId(MemberId.from(nodeConfig.nodeId()))
            .withAddress(Address.from(nodeConfig.address(), nodeConfig.port()))
            .withMulticastEnabled()
            .withManagementGroup(buildManagementGroup())
            .withMembershipProtocol(buildMembershipProtocol())
            .withPartitionGroups(buildRaftPartition())
            .setBroadcastInterval(
                Duration.ofSeconds(
                    clusterConfig.getConfig(ConfigKey.CLUSTER_BOOTSTRAP_DISCOVERY_BROADCAST_INTERVAL_SECONDS, 1L)))
            .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private RaftPartitionGroup buildManagementGroup() {
        return RaftPartitionGroup
            .builder("system")
            .withNumPartitions(
                clusterConfig.getConfig(ConfigKey.CLUSTER_MGR_PARTITIONS, 1))
            .withStorageLevel(
                StorageLevel.MAPPED)
            .withDataDirectory(
                new File(clusterConfig.getConfig(ConfigKey.CLUSTER_MGR_DATA_DIR, "./commander-mgr-data")))
            .withMembers(
                clusterConfig.getConfig(ConfigKey.CLUSTER_MGR_GROUP_MEMBERS, EMPTY_STRING_ARRAY))
            .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private GroupMembershipProtocol buildMembershipProtocol() {
        return SwimMembershipProtocol.builder()
            .withFailureTimeout(
                Duration.ofSeconds(
                    clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_FAILURE_TIME_OUT_SECONDS, 10L)))
            .withGossipInterval(
                Duration.ofMillis(
                    clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_GOSSIP_INTERVAL_MS, 250L)))
            .withProbeInterval(
                Duration.ofSeconds(
                    clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_PROBE_INTERVAL_SECONDS, 1L)))
            .withBroadcastDisputes(
                clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_BROADCAST_DISPUTES, false))
            .withBroadcastUpdates(
                clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_BROADCAST_UPDATES, false))
            .withGossipFanout(
                clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_GOOSIP_FANOUT, 2))
            .withNotifySuspect(
                clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_NOTIFY_SUSPECT, false))
            .withSuspectProbes(
                clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_SUSPECT_PROBES, 2))
            .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private ManagedPartitionGroup buildRaftPartition() {
        return RaftPartitionGroup
            .builder("raft-partition")
            .withNumPartitions(
                clusterConfig.getConfig(ConfigKey.CLUSTER_RAFT_PARTITION_PARTITIONS, 7))
            .withPartitionSize(
                clusterConfig.getConfig(ConfigKey.CLUSTER_RAFT_PARTITION_PARTITION_SIZE, 0))
            .withStorageLevel(
                StorageLevel.DISK)
            .withDataDirectory(
                new File(clusterConfig.getConfig(ConfigKey.CLUSTER_RAFT_PARTITION_DATA_DIR,
                    "./commander-raft-partition-data")))
            .withMembers(
                clusterConfig.getConfig(ConfigKey.CLUSTER_RAFT_PARTITION_MEMBERS, EMPTY_STRING_ARRAY))
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
