package com.github.ynfeng.commander.communicate.impl;

import io.netty.buffer.ByteBuf;
import java.util.Arrays;

public abstract class ProtocolMessage {
    private long messageId;
    private byte[] payload;
    private Type type;

    protected ProtocolMessage(Type type) {
        this.type = type;
    }

    protected ProtocolMessage(long messageId, Type type, byte[] payload) {
        this.messageId = messageId;
        this.payload = payload.clone();
        this.type = type;
    }

    public byte[] payload() {
        return payload.clone();
    }

    public long messageId() {
        return messageId;
    }

    abstract void encode(ByteBuf out);

    abstract void decode(ByteBuf in);

    public Type type() {
        return type;
    }

    enum Type {
        REQUEST((byte) 1),
        RESPONSE((byte) 2);
        private final byte value;

        Type(byte value) {
            this.value = value;
        }

        public static Type of(byte value) {
            return Arrays.stream(values())
                .filter(each -> each.value == value)
                .findFirst()
                .orElse(null);
        }

        public byte value() {
            return value;
        }
    }

    protected void messageId(long messageId) {
        this.messageId = messageId;
    }

    protected void payload(byte[] payload) {
        this.payload = payload.clone();
    }
}
