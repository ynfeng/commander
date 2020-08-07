package com.github.ynfeng.commander.cluster;

public interface LifeCyle {
    void preStart();

    void start();

    void postStart();

    void preStop();

    void stop();

    void postStop();
}
