package com.github.ynfeng.commander.cluster;

public interface ClusterController extends LifeCyle {
    void startup();

    void shutdown();
}
