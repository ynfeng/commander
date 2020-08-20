package com.github.ynfeng.commander.cluster;


import com.github.ynfeng.commander.support.env.Environment;

public interface ClusterProvider {

    Cluster getCluster(Environment environment);

    Environment parepareEnvironment();
}
