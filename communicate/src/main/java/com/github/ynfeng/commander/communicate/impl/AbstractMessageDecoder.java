package com.github.ynfeng.commander.communicate.impl;

import io.netty.handler.codec.ByteToMessageDecoder;

public abstract class AbstractMessageDecoder extends ByteToMessageDecoder {

    public abstract ProtocolMessage createByType(ProtocolMessage.Type type);
}
