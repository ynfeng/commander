package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.AbstractCluster;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;
import io.atomix.cluster.MemberId;
import io.atomix.cluster.protocol.GroupMembershipProtocol;
import io.atomix.cluster.protocol.SwimMembershipProtocol;
import io.atomix.core.Atomix;
import io.atomix.protocols.raft.partition.RaftPartitionGroup;
import io.atomix.storage.StorageLevel;
import io.atomix.utils.net.Address;
import java.io.File;
import java.time.Duration;

public class AtomixCluster extends AbstractCluster {
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
            .setBroadcastInterval(
                Duration.ofSeconds(
                    clusterConfig.getConfig(ConfigKey.CLUSTER_BOOTSTRAP_DISCOVERY_BROADCAST_INTERVAL_SECONDS)))
            .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private GroupMembershipProtocol buildMembershipProtocol() {
        return SwimMembershipProtocol.builder()
            .withFailureTimeout(
                Duration.ofSeconds(
                    clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_FAILURE_TIME_OUT_SECONDS)))
            .withGossipInterval(
                Duration.ofSeconds(
                    clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_GOSSIP_INTERVAL_SECONDS)))
            .withProbeInterval(
                Duration.ofSeconds(
                    clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_PROBE_INTERVAL_SECONDS)))
            .withBroadcastDisputes(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_BROADCAST_DISPUTES))
            .withBroadcastUpdates(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_BROADCAST_UPDATES))
            .withGossipFanout(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_GOOSIP_FANOUT))
            .withNotifySuspect(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_NOTIFY_SUSPECT))
            .withSuspectProbes(clusterConfig.getConfig(ConfigKey.CLUSTER_MEMBERSHIP_SUSPECT_PROBES))
            .build();
    }

    private RaftPartitionGroup buildManagementGroup() {
        return RaftPartitionGroup
            .builder("system")
            .withNumPartitions(clusterConfig.getConfig(ConfigKey.CLUSTER_MGR_PARTITIONS))
            .withStorageLevel(StorageLevel.MAPPED)
            .withDataDirectory(new File((String) clusterConfig.getConfig(ConfigKey.CLUSTER_MGR_DATA_DIR)))
            .withMembers((String[]) clusterConfig.getConfig(ConfigKey.CLUSTER_MGR_GROUP_MEMBERS))
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
