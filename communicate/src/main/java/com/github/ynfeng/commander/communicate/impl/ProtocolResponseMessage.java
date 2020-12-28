package com.github.ynfeng.commander.communicate.impl;

import io.netty.buffer.ByteBuf;
import java.util.Arrays;

public class ProtocolResponseMessage extends ProtocolMessage {
    private Status status;

    private ProtocolResponseMessage(long messageId, Status status, byte[] payload) {
        super(messageId, Type.RESPONSE, payload);
        this.status = status;
    }

    public ProtocolResponseMessage(Type type) {
        super(type);
    }

    @Override
    void encode(ByteBuf out) {
        int len = 14;
        len += payload().length;
        out.writeInt(len);
        out.writeByte(type().value());
        out.writeLong(messageId());
        out.writeByte(status().value);
        out.writeInt(payload().length);
        out.writeBytes(payload());
    }

    @Override
    void decode(ByteBuf in) {
        readMessageId(in);
        readStatus(in);
        readPayload(in);
    }

    private void readPayload(ByteBuf in) {
        byte[] payload = new byte[in.readInt()];
        in.readBytes(payload);
        payload(payload);
    }


    private void readStatus(ByteBuf in) {
        status = Status.of(in.readByte());
    }

    private void readMessageId(ByteBuf in) {
        messageId(in.readLong());
    }

    public static ProtocolResponseMessage ok(long messageId, byte[] reply) {
        return new ProtocolResponseMessage(messageId, ProtocolResponseMessage.Status.OK, reply);
    }

    public Status status() {
        return status;
    }

    enum Status {
        OK((byte) 1);
        private final byte value;

        Status(byte value) {
            this.value = value;
        }

        public static Status of(byte value) {
            return Arrays.stream(values())
                .filter(each -> each.value == value)
                .findFirst()
                .orElse(null);
        }

        public byte value() {
            return value;
        }
    }
}
