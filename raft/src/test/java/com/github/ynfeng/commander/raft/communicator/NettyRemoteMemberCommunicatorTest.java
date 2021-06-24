package com.github.ynfeng.commander.raft.communicator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.fixture.RaftMemberDiscoveryStub;
import com.github.ynfeng.commander.fixture.RemoteMembers;
import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RaftConfig;
import com.github.ynfeng.commander.raft.RaftGroup;
import com.github.ynfeng.commander.raft.RaftServer;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;
import com.github.ynfeng.commander.support.Address;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Disabled;
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

    @Test
    @Disabled
    void should_elected_leader() {
        RaftGroup raftGroup = RaftGroup.create()
            .addMemberId(MemberId.create("server1"))
            .addMemberId(MemberId.create("server2"))
            .addMemberId(MemberId.create("server3"))
            .addMemberId(MemberId.create("server4"))
            .addMemberId(MemberId.create("server5"));

        RaftServer raftServer1 = createRaftServer(MemberId.create("server1"), Address.of("127.0.0.1", 8111), raftGroup, 0, 5);
        RaftServer raftServer2 = createRaftServer(MemberId.create("server2"), Address.of("127.0.0.1", 8112), raftGroup, 1, 5);
        RaftServer raftServer3 = createRaftServer(MemberId.create("server3"), Address.of("127.0.0.1", 8113), raftGroup, 2, 5);
        RaftServer raftServer4 = createRaftServer(MemberId.create("server4"), Address.of("127.0.0.1", 8114), raftGroup, 3, 5);
        RaftServer raftServer5 = createRaftServer(MemberId.create("server5"), Address.of("127.0.0.1", 8115), raftGroup, 4, 5);

        raftServer1.start();
        raftServer2.start();
        raftServer3.start();
        raftServer4.start();
        raftServer5.start();

        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
        }
    }

    private static RaftServer createRaftServer(MemberId localMemberId,
                                               Address localAddress,
                                               RaftGroup raftGroup,
                                               int currentIdex,
                                               int size) {
        RemoteMemberCommunicator remoteMemberCommunicator = createNettyRemoteMemberCommunicator("cluster1", localAddress);
        return createRaftServer(remoteMemberCommunicator, localMemberId, localAddress, raftGroup, currentIdex, size);
    }

    private static RemoteMemberCommunicator createNettyRemoteMemberCommunicator(String clusterId, Address localAddress) {
        return new NettyRemoteMemberCommunicator(clusterId, localAddress);
    }

    private static RaftServer createRaftServer(RemoteMemberCommunicator communicator,
                                               MemberId localMemberId,
                                               Address localAddress,
                                               RaftGroup raftGroup,
                                               int currentIndex,
                                               int size) {
        RaftMemberDiscoveryStub raftMemberDiscovery = new RaftMemberDiscoveryStub();
        raftMemberDiscovery.addRemoteMember(RemoteMembers.getRemoteMembers(currentIndex, size));
        return RaftServer.builder()
            .localAddress(localAddress)
            .localMemberId(localMemberId)
            .raftConfig(RaftConfig.builder()
                .electionTimeout(1000)
                .leaderHeartbeatInterval(100)
                .serverThreadPoolSize(3)
                .build())
            .raftMemberDiscovery(raftMemberDiscovery)
            .remoteMemberCommunicator(communicator)
            .raftGroup(raftGroup)
            .build();
    }
}
