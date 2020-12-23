package com.github.ynfeng.commander.cluster.communicate.impl;

public interface ServerConnection extends Connection {
    void reply(ProtocolMessage message);
}
