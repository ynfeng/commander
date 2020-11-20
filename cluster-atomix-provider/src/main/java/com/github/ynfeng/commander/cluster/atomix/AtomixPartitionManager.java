package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.Partition;
import com.github.ynfeng.commander.cluster.PartitionManager;
import io.atomix.cluster.MemberId;
import io.atomix.core.Atomix;
import io.atomix.protocols.raft.partition.RaftPartition;
import java.util.List;
import java.util.stream.Collectors;

public class AtomixPartitionManager implements PartitionManager {
    private Atomix atomix;
    private List<RaftPartition> owningPartitions;

    public AtomixPartitionManager(Atomix atomix) {
        this.atomix = atomix;
        init();
    }

    private void init() {
        owningPartitions = getOwningPartitions();
//        for (RaftPartition partition : owningPartitions) {
//            partition.addRoleChangeListener(role -> {
//
//            });
//        }
    }

    private List<RaftPartition> getOwningPartitions() {
        MemberId nodeId = atomix.getMembershipService().getLocalMember().id();
        return atomix.getPartitionService().getPartitionGroup(AtomixCluster.RAFT_PARTITION_GROUP_NAME)
            .getPartitions().stream()
            .filter(partition -> partition.members().contains(nodeId))
            .map(RaftPartition.class::cast)
            .collect(Collectors.toList());
    }

    @Override
    public List<Partition> getLocalLeaderPartitions() {
        return owningPartitions.stream()
            .filter(RaftPartition::isLeader)
            .map(rp -> new Partition(rp.name()))
            .collect(Collectors.toList());
    }
}
