package com.github.ynfeng.commander.cluster;


public interface ClusterProvider {

    Cluster getCluster(Environment environment);

    Environment parepareEnvironment();
}
