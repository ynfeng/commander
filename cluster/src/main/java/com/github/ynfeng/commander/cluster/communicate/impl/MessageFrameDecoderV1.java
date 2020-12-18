package com.github.ynfeng.commander.cluster.communicate.impl;

public class MessageFrameDecoderV1 extends AbstractFrameMessageDecoder {
    public MessageFrameDecoderV1() {
        super(Integer.MAX_VALUE, 0, 4, 0, 4, false);
    }
}
