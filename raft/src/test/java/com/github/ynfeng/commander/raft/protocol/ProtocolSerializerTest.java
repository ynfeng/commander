package com.github.ynfeng.commander.raft.protocol;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;
import org.junit.jupiter.api.Test;

class ProtocolSerializerTest {
    ProtocolSerializer protocolSerializer = new ProtocolSerializer();

    @Test
    void should_encode_and_decode_vote_request() {
        VoteRequest voteRequest = VoteRequest.builder()
            .term(Term.create(100))
            .candidateId(MemberId.create("peer1"))
            .lastLogIndex(200)
            .lastLogTerm(Term.create(101))
            .build();

        byte[] bytes = protocolSerializer.encode(voteRequest);
        voteRequest = protocolSerializer.decode(bytes);

        assertThat(voteRequest.term(), is(Term.create(100)));
    }

    @Test
    void should_encode_and_decode_vote_response() {
        RequestVoteResponse response = RequestVoteResponse.voted(Term.create(1), MemberId.create("peer1"));
        byte[] bytes = protocolSerializer.encode(response);
        response = protocolSerializer.decode(bytes);

        assertThat(response.isVoted(), is(true));
    }

    @Test
    void should_encode_and_decode_leader_heartbeat() {
        LeaderHeartbeat leaderHeartbeat = LeaderHeartbeat.builder()
            .leaderCommit(1)
            .leaderId(MemberId.create("1"))
            .term(Term.create(1))
            .prevLogTerm(Term.create(1))
            .prevLogIndex(1)
            .build();

        byte[] bytes = protocolSerializer.encode(leaderHeartbeat);
        leaderHeartbeat = protocolSerializer.decode(bytes);

        assertThat(leaderHeartbeat.leaderId(), is(MemberId.create("1")));
    }

    @Test
    void should_encode_and_decode_empty_response() {
        EmptyResponse emptyResponse = new EmptyResponse();

        byte[] bytes = protocolSerializer.encode(emptyResponse);
        emptyResponse = protocolSerializer.decode(bytes);

        assertThat(emptyResponse, notNullValue());
    }
}
