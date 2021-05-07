package com.github.ynfeng.commander.raft;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.fixture.FakeRemoteMemberCommunicator;
import com.github.ynfeng.commander.fixture.RaftMemberDiscoveryStub;
import com.github.ynfeng.commander.fixture.RemoteMemberCommunicatorHub;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.support.Address;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RaftServerTest {
    private static final RemoteMember REMOTE_MEMBER1 = RemoteMember.create(MemberId.create("server2"), Address.of("127.0.0.1", 8081));
    private static final RemoteMember REMOTE_MEMBER2 = RemoteMember.create(MemberId.create("server2"), Address.of("127.0.0.1", 8082));
    private static final RemoteMember REMOTE_MEMBER3 = RemoteMember.create(MemberId.create("server3"), Address.of("127.0.0.1", 8083));
    private final RemoteMemberCommunicatorHub communicatorHub = new RemoteMemberCommunicatorHub();

    @BeforeEach
    void setup() {
        communicatorHub.reset();
    }

    @Test
    void should_start_raft_server() {
        RaftServer raftServer = createRaftServer(new FakeRemoteMemberCommunicator(communicatorHub), MemberId.create("server1"), REMOTE_MEMBER2, REMOTE_MEMBER3);

        raftServer.start();

        await()
            .atMost(1, TimeUnit.SECONDS)
            .until(raftServer::isStarted, is(true));
    }

    @Test
    void should_elect_leader() {
        FakeRemoteMemberCommunicator server1Communicator = new FakeRemoteMemberCommunicator(communicatorHub);
        communicatorHub.registerCommunicator(MemberId.create("server1"), server1Communicator);
        RaftServer raftServer1 = createRaftServer(server1Communicator, MemberId.create("server1"), REMOTE_MEMBER2, REMOTE_MEMBER3);

        FakeRemoteMemberCommunicator server2Communicator = new FakeRemoteMemberCommunicator(communicatorHub);
        communicatorHub.registerCommunicator(MemberId.create("server2"), server2Communicator);
        RaftServer raftServer2 = createRaftServer(server2Communicator, MemberId.create("server2"), REMOTE_MEMBER1, REMOTE_MEMBER3);

        FakeRemoteMemberCommunicator server3Communicator = new FakeRemoteMemberCommunicator(communicatorHub);
        communicatorHub.registerCommunicator(MemberId.create("server3"), server3Communicator);
        RaftServer raftServer3 = createRaftServer(server3Communicator, MemberId.create("server3"), REMOTE_MEMBER1, REMOTE_MEMBER2);

        raftServer1.start();
        raftServer2.start();
        raftServer3.start();

        LeaderHeartbeat heartbeat = server2Communicator.expectRequest(LeaderHeartbeat.class);
        assertThat(heartbeat.term(), is(Term.create(1)));
        assertThat(heartbeat.prevLogIndex(), is(0L));
        assertThat(heartbeat.prevLogTerm(), is(Term.create(0)));
        assertThat(heartbeat.leaderCommit(), is(0L));
    }

    private static RaftServer createRaftServer(RemoteMemberCommunicator communicator, MemberId localMemberId, RemoteMember... remoteMembers) {
        RaftMemberDiscoveryStub raftMemberDiscovery = new RaftMemberDiscoveryStub();
        raftMemberDiscovery.addRemoteMember(remoteMembers);
        return RaftServer.builder()
            .localAddress(Address.of("127.0.0.1", 8080))
            .localMemberId(localMemberId)
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
