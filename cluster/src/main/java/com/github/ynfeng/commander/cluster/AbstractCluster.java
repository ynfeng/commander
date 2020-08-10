package com.github.ynfeng.commander.cluster;

public abstract class AbstractCluster implements LifeCyle, Cluster {
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
