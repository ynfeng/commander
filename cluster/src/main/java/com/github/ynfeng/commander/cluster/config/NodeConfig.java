package com.github.ynfeng.commander.cluster.config;

import com.github.ynfeng.commander.support.CmderConfig;

public interface NodeConfig extends CmderConfig {
    String nodeId();

    String address();

    int port();
}
