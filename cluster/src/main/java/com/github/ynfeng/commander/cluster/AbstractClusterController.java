package com.github.ynfeng.commander.cluster;

public abstract class AbstractClusterController implements ClusterController {
    @Override
    public void preStart() {
    }

    @Override
    public void postStart() {
    }

    @Override
    public void preStop() {
    }

    @Override
    public void postStop() {
    }

    @Override
    public void startup() {
        preStart();
        start();
        postStart();
    }

    @Override
    public void shutdown() {
        preStop();
        stop();
        postStop();
    }
}
