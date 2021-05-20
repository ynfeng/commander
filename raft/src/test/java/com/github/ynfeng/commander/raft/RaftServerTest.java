package com.github.ynfeng.commander.raft;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;

import com.github.ynfeng.commander.fixture.FakeRemoteMemberCommunicator;
import com.github.ynfeng.commander.fixture.RaftMemberDiscoveryStub;
import com.github.ynfeng.commander.fixture.RemoteMemberCommunicatorHub;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.support.Address;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class RaftServerTest {
    private static final RemoteMember REMOTE_MEMBER1 = RemoteMember.create(MemberId.create("server1"), Address.of("127.0.0.1", 8081));
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
    @Disabled
    void should_receive_heartbeat_after_leader_elected() {
        FakeRemoteMemberCommunicator server1Communicator = new FakeRemoteMemberCommunicator(communicatorHub);
        communicatorHub.registerCommunicator(MemberId.create("server1"), server1Communicator);
        RaftServer raftServer1 = createRaftServer(server1Communicator, MemberId.create("server1"), REMOTE_MEMBER2, REMOTE_MEMBER3);

        FakeRemoteMemberCommunicator server2Communicator = new FakeRemoteMemberCommunicator(communicatorHub);
        communicatorHub.registerCommunicator(MemberId.create("server2"), server2Communicator);
        RaftServer raftServer2 = createRaftServer(server2Communicator, MemberId.create("server2"), REMOTE_MEMBER1, REMOTE_MEMBER3);

        FakeRemoteMemberCommunicator server3Communicator = new FakeRemoteMemberCommunicator(communicatorHub);
        communicatorHub.registerCommunicator(MemberId.create("server3"), server3Communicator);
        RaftServer raftServer3 = createRaftServer(server3Communicator, MemberId.create("server3"), REMOTE_MEMBER1, REMOTE_MEMBER2);

        raftServer3.start();
        raftServer2.start();
        raftServer1.start();

        server2Communicator.expectRequest(LeaderHeartbeat.class);
    }

    @Test
    void should_next_term_when_become_candicate() {
        RaftServer raftServer = createRaftServer(new FakeRemoteMemberCommunicator(communicatorHub), MemberId.create("server1"), REMOTE_MEMBER2, REMOTE_MEMBER3);

        raftServer.start();

        await()
            .until(raftServer::currentTerm, is(Term.create(1)));
    }

    private static RaftServer createRaftServer(RemoteMemberCommunicator communicator, MemberId localMemberId, RemoteMember... remoteMembers) {
        RaftMemberDiscoveryStub raftMemberDiscovery = new RaftMemberDiscoveryStub();
        raftMemberDiscovery.addRemoteMember(remoteMembers);
        return RaftServer.builder()
            .localAddress(Address.of("127.0.0.1", 8080))
            .localMemberId(localMemberId)
            .raftConfig(RaftConfig.builder()
                .electionTimeout(1000)
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
