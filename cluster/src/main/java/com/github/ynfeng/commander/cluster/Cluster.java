package com.github.ynfeng.commander.cluster;

public abstract class Cluster implements LifeCyle {
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

    public void startup() {
        preStart();
        start();
        postStart();
    }

    public void shutdown() {
        preStop();
        stop();
        postStop();
    }
}
