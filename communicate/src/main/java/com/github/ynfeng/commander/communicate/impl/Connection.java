package com.github.ynfeng.commander.communicate.impl;

public interface Connection<M extends ProtocolMessage> {
    void dispatch(M message);
}
