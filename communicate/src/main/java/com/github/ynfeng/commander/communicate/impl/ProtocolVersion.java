package com.github.ynfeng.commander.communicate.impl;

import java.util.Arrays;
import java.util.Comparator;
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

    public static ProtocolVersion lastest() {
        return Arrays.stream(values()).max(Comparator.comparingInt(v -> v.version)).orElse(null);
    }

    public abstract AbstractMessageEncoder newEncoder();

    public abstract AbstractMessageDecoder newDecoder();

    public abstract AbstractFrameMessageDecoder newFrameDecoder();

    public byte version() {
        return version;
    }
}
