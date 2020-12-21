package com.github.ynfeng.commander.cluster.communicate.impl;

import io.netty.handler.codec.MessageToByteEncoder;

public abstract class AbstractMessageEncoder extends MessageToByteEncoder<Object> {
    @Override
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return msg instanceof ProtocolMessage;
    }
}
