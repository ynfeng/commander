package com.github.ynfeng.commander.raft.protocol;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.serializer.SerializationTypes;
import com.github.ynfeng.commander.serializer.Serializer;
import com.github.ynfeng.commander.support.Address;

public class ProtocolSerializer {
    private final Serializer serializer =
        Serializer.create(SerializationTypes.builder()
            .add(SerializationTypes.BASIC)
            .add(EmptyResponse.class)
            .add(VoteRequest.class)
            .add(RequestVoteResponse.class)
            .add(Term.class)
            .add(Address.class)
            .add(MemberId.class)
            .build());

    public byte[] encode(Object obj) {
        return serializer.encode(obj);
    }

    public <T> T decode(byte[] bytes) {
        return serializer.decode(bytes);
    }
}
