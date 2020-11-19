package com.github.ynfeng.commander.cluster;

import java.util.List;

public interface PartitionManager {
    List<Partition> getLocalLeaderPartitions();
}
