package com.github.ynfeng.commander.communicate.impl;

public interface ServerConnection extends Connection<ProtocolRequestMessage> {
    void reply(ProtocolResponseMessage response);
}
