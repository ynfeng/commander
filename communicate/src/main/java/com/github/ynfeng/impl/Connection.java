package com.github.ynfeng.commander.cluster.communicate.impl;

public interface Connection<M extends ProtocolMessage> {
    void dispatch(M message);
}
