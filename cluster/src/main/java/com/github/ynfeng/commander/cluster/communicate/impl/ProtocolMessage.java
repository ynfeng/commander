package com.github.ynfeng.commander.cluster.communicate.impl;

import com.github.ynfeng.commander.support.Address;

public class ProtocolMessage {
    private Address address;
    private String subject;
    private byte[] payload;

    public static ProtocolMessageBuilder builder() {
        return new ProtocolMessageBuilder();
    }

    public Address address() {
        return address;
    }

    public byte[] payload() {
        return payload.clone();
    }

    public String subject() {
        return subject;
    }

    static class ProtocolMessageBuilder {
        private final ProtocolMessage protocolMessage = new ProtocolMessage();

        public ProtocolMessageBuilder address(Address address) {
            protocolMessage.address = address;
            return this;
        }

        public ProtocolMessageBuilder subject(String subject) {
            protocolMessage.subject = subject;
            return this;
        }

        public ProtocolMessageBuilder payload(byte[] payload) {
            protocolMessage.payload = payload.clone();
            return this;
        }

        public ProtocolMessage build() {
            return protocolMessage;
        }
    }
}
