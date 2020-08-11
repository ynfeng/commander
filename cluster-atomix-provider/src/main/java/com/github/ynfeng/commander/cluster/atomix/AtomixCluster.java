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
            .setBroadcastInterval(Duration.ofSeconds(clusterConfig.bootstrapDiscoveryBroadcastIntervalSeconds()))
            .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private GroupMembershipProtocol buildMembershipProtocol() {
        return SwimMembershipProtocol.builder()
//            .withFailureTimeout(membershipCfg.getFailureTimeout())
//            .withGossipInterval(membershipCfg.getGossipInterval())
//            .withProbeInterval(membershipCfg.getProbeInterval())
//            .withProbeTimeout(membershipCfg.getProbeTimeout())
//            .withBroadcastDisputes(membershipCfg.isBroadcastDisputes())
//            .withBroadcastUpdates(membershipCfg.isBroadcastUpdates())
//            .withGossipFanout(membershipCfg.getGossipFanout())
//            .withNotifySuspect(membershipCfg.isNotifySuspect())
//            .withSuspectProbes(membershipCfg.getSuspectProbes())
//            .withSyncInterval(membershipCfg.getSyncInterval())
            .build();
    }

    private RaftPartitionGroup buildManagementGroup() {
        return RaftPartitionGroup
            .builder("system")
            .withNumPartitions(clusterConfig.managementPartitions())
            .withStorageLevel(StorageLevel.MAPPED)
            .withDataDirectory(new File(clusterConfig.managementDataDir()))
            .withMembers(clusterConfig.managementGroupMembers())
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
