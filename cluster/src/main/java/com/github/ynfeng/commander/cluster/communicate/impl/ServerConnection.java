package com.github.ynfeng.commander.cluster.communicate.impl;

public interface ServerConnection extends Connection<ProtocolRequestMessage> {
    void reply(ProtocolResponseMessage response);
}
