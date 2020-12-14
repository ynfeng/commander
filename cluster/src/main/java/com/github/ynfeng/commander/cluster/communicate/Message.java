package com.github.ynfeng.commander.cluster.communicate;

public class Message {
    private final String type;
    private final byte[] payload;

    public Message(String type, byte[] payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public byte[] getPayload() {
        return payload;
    }
}
