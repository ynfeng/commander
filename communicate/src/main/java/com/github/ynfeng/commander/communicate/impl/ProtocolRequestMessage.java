package com.github.ynfeng.commander.communicate.impl;

import com.github.ynfeng.commander.support.Address;
import io.netty.buffer.ByteBuf;

public class ProtocolRequestMessage extends ProtocolMessage {
    private String subject;
    private Address senderAddress;

    public ProtocolRequestMessage(Type type) {
        super(type);
    }

    protected ProtocolRequestMessage(String subject, Address senderAddress, byte[] payload) {
        super(MessageIdGenerator.nextId(), Type.REQUEST, payload);
        this.senderAddress = senderAddress;
        this.subject = subject;
    }

    public String subject() {
        return subject;
    }

    public Address senderAddress() {
        return senderAddress;
    }

    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    void encode(ByteBuf out) {
        int len = 20;
        byte[] hostBytes = senderAddress().host().getBytes();
        len += hostBytes.length;
        byte[] subjectBytes = subject().getBytes();
        len += subjectBytes.length;
        byte[] payload = payload();
        len += payload.length;
        out.writeInt(len);
        out.writeByte(type().value());
        out.writeLong(messageId());
        out.writeByte(hostBytes.length);
        out.writeBytes(hostBytes);
        out.writeInt(senderAddress().port());
        out.writeShort(subjectBytes.length);
        out.writeBytes(subjectBytes);
        out.writeInt(payload.length);
        out.writeBytes(payload);
    }

    @Override
    void decode(ByteBuf in) {
        readMessageId(in);
        readAddress(in);
        readSubject(in);
        readPayload(in);
    }

    private void readMessageId(ByteBuf in) {
        messageId(in.readLong());
    }

    private void readAddress(ByteBuf in) {
        byte hostLen = in.readByte();
        byte[] hostBytes = new byte[hostLen];
        in.readBytes(hostBytes);
        int port = in.readInt();
        String host = new String(hostBytes);
        senderAddress = Address.of(host, port);
    }

    private void readSubject(ByteBuf in) {
        short subjectLen = in.readShort();
        byte[] subjectBytes = new byte[subjectLen];
        in.readBytes(subjectBytes);
        subject = new String(subjectBytes);
    }

    private void readPayload(ByteBuf in) {
        int payloadLen = in.readInt();
        byte[] payload = new byte[payloadLen];
        in.readBytes(payload);
        payload(payload);
    }
}

