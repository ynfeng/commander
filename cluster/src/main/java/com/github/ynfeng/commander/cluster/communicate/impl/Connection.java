package com.github.ynfeng.commander.cluster.communicate.impl;

public interface Connection {
    void dispatch(ProtocolMessage protocolMessage);
}
