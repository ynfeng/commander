package com.github.ynfeng.commander.cluster.communicate.protocol;

import com.github.ynfeng.commander.cluster.communicate.impl.AbstractMessageDecoder;
import com.github.ynfeng.commander.cluster.communicate.impl.AbstractMessageEncoder;
import com.github.ynfeng.commander.cluster.communicate.impl.MessageDecoderV1;
import com.github.ynfeng.commander.cluster.communicate.impl.MessageEncoderV1;
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

    public byte version() {
        return version;
    }
}
