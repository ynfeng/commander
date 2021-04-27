package com.github.ynfeng.commander.raft;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.RequestVote;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.Response;
import com.github.ynfeng.commander.support.Address;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class RaftServerTest {
    public static final RemoteMember REMOTE_MEMBER2 = RemoteMember.create(MemberId.create("server2"), Address.of("127.0.0.1", 8082));
    public static final RemoteMember REMOTE_MEMBER3 = RemoteMember.create(MemberId.create("server3"), Address.of("127.0.0.1", 8083));

    @Test
    void should_start_raft_server() {
        RaftServer raftServer = createRaftServer(new RemoteMemberCommunicatorStub());

        raftServer.start();

        await()
            .atMost(1, TimeUnit.SECONDS)
            .until(raftServer::isStarted, is(true));
    }

    @Test
    void should_become_leader_and_send_leader_heartbeat() {
        AtomicReference<LeaderHeartbeat> hbref = new AtomicReference<>();
        RaftServer raftServer = createRaftServer(new RemoteMemberCommunicator() {
            @SuppressWarnings("unchecked")
            @Override
            public <T extends Request, R extends Response> CompletableFuture<R> send(RemoteMember remoteMember, T request) {
                if (request instanceof RequestVote) {
                    CompletableFuture<RequestVoteResponse> completableFuture = new CompletableFuture<>();
                    RequestVoteResponse response = RequestVoteResponse.granted(Term.create(0), MemberId.create(UUID.randomUUID().toString()));
                    completableFuture.complete(response);
                    return (CompletableFuture<R>) completableFuture;
                }

                if (request instanceof LeaderHeartbeat) {
                    System.out.println();
                    hbref.set((LeaderHeartbeat) request);
                }

                return new CompletableFuture<>();
            }
        });

        raftServer.start();

        await().until(() -> hbref.get() != null, is(true));

        LeaderHeartbeat heartbeat = hbref.get();
        assertThat(heartbeat.leaderId(), is(MemberId.create("server1")));
        assertThat(heartbeat.term(), is(Term.create(1)));
        assertThat(heartbeat.prevLogIndex(), is(0L));
        assertThat(heartbeat.prevLogTerm(), is(Term.create(0)));
        assertThat(heartbeat.leaderCommit(), is(0L));
    }

    private static RaftServer createRaftServer(RemoteMemberCommunicator communicator) {
        RaftMemberDiscoveryStub raftMemberDiscovery = new RaftMemberDiscoveryStub();
        raftMemberDiscovery.addRemoteMember(REMOTE_MEMBER2, REMOTE_MEMBER3);
        return RaftServer.builder()
            .localAddress(Address.of("127.0.0.1", 8080))
            .localMemberId(MemberId.create("server1"))
            .raftConfig(RaftConfig.builder()
                .electionTimeout(500)
                .leaderHeartbeatInterval(100)
                .serverThreadPoolSize(3)
                .build())
            .raftMemberDiscovery(raftMemberDiscovery)
            .remoteMemberCommunicator(communicator)
            .raftGroup(RaftGroup.create()
                .addMemberId(MemberId.create("server1"))
                .addMemberId(MemberId.create("server2"))
                .addMemberId(MemberId.create("server3")))
            .build();
    }
}
