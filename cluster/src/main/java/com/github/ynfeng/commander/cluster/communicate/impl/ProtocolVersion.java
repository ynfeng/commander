package com.github.ynfeng.commander.cluster.communicate.impl;

import java.util.Arrays;
import java.util.Optional;

public enum ProtocolVersion {
    V1(1) {
        @Override
        public AbstractMessageEncoder newEncoder() {
            return new MessageEncoderV1();
        }

        @Override
        public AbstractMessageDecoder newDecoder() {
            return new MessageDecoderV1();
        }

        @Override
        public AbstractFrameMessageDecoder newFrameDecoder() {
            return new MessageFrameDecoderV1();
        }
    };
    private final byte version;

    ProtocolVersion(int version) {
        this.version = (byte) version;
    }

    public static Optional<ProtocolVersion> valueOf(byte version) {
        return Arrays.stream(values())
            .filter(pv -> pv.version == version)
            .findFirst();
    }

    public abstract AbstractMessageEncoder newEncoder();

    public abstract AbstractMessageDecoder newDecoder();

    public abstract AbstractFrameMessageDecoder newFrameDecoder();

    public byte version() {
        return version;
    }
}
