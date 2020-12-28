package com.github.ynfeng.commander.communicate.impl;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public abstract class AbstractFrameMessageDecoder extends LengthFieldBasedFrameDecoder {
    @SuppressWarnings("checkstyle:ParameterNumber")
    protected AbstractFrameMessageDecoder(int maxFrameLength,
                                          int lengthFieldOffset,
                                          int lengthFieldLength,
                                          int lengthAdjustment,
                                          int initialBytesToStrip,
                                          boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }
}
