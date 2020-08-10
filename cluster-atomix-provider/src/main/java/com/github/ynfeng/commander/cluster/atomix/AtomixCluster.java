package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.AbstractCluster;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;
import io.atomix.cluster.MemberId;
import io.atomix.core.Atomix;
import io.atomix.core.AtomixBuilder;
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

    private void initAtomix() {
        AtomixBuilder builder = Atomix.builder();
        builder.withClusterId(clusterConfig.clusterId());
        builder.withMemberId(MemberId.from(nodeConfig.nodeId()));
        builder.withAddress(Address.from(nodeConfig.address(), nodeConfig.port()));
        builder.withMulticastEnabled();
        builder.withManagementGroup(buildManagementGroup());
        builder.setBroadcastInterval(Duration.ofSeconds(clusterConfig.bootstrapDiscoveryBroadcastIntervalSeconds()));
        atomix = builder.build();
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
