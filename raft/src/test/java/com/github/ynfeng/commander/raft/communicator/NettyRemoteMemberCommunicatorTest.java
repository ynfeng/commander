package com.github.ynfeng.commander.raft.communicator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;
import com.github.ynfeng.commander.support.Address;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

class NettyRemoteMemberCommunicatorTest {
    private static final MemberId MEMBER_PEER_1 = MemberId.create("peer1");
    private static final MemberId MEMBER_PEER_2 = MemberId.create("peer2");
    private static final Address ADDRESS_8111 = Address.of("127.0.0.1", 8111);
    private static final Address ADDRESS_8112 = Address.of("127.0.0.1", 8112);
    private static final RemoteMember REMOTE_MEMBER_PEER_1 = RemoteMember.create(MEMBER_PEER_1, ADDRESS_8111);

    @Test
    void should_send_and_receive() throws Exception {
        RemoteMemberCommunicator peer1 = new NettyRemoteMemberCommunicator("cluster1", ADDRESS_8111);
        peer1.registerHandler(VoteRequest.class, voteRequest -> {
            return RequestVoteResponse.voted(Term.create(1), MEMBER_PEER_1);
        });

        RemoteMemberCommunicator peer2 = new NettyRemoteMemberCommunicator("cluster1", ADDRESS_8112);

        peer1.start();
        peer2.start();

        try {
            CompletableFuture<RequestVoteResponse> responseFuture = peer2.send(REMOTE_MEMBER_PEER_1, VoteRequest.builder()
                .term(Term.create(100))
                .candidateId(MEMBER_PEER_2)
                .lastLogIndex(200)
                .lastLogTerm(Term.create(101))
                .build());

            RequestVoteResponse voteResponse = responseFuture.get();
            assertThat(voteResponse.isVoted(), is(true));
            assertThat(voteResponse.voterId(), is(MEMBER_PEER_1));
        } finally {
            peer1.shutdown();
            peer2.shutdown();
        }
    }
}
