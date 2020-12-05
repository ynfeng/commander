package com.github.ynfeng.commander.support;

public interface ProtocolType<C extends Config, T> {

    T newProtocol(C config);
}
