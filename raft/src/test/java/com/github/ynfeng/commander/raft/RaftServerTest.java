package com.github.ynfeng.commander.raft;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.fixture.FakeRemoteMemberCommunicator;
import com.github.ynfeng.commander.fixture.RaftMemberDiscoveryStub;
import com.github.ynfeng.commander.fixture.RemoteMemberCommunicatorHub;
import com.github.ynfeng.commander.fixture.RemoteMemberCommunicatorSpy;
import com.github.ynfeng.commander.fixture.RemoteMembers;
import com.github.ynfeng.commander.support.Address;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RaftServerTest {
    private final RemoteMemberCommunicatorHub communicatorHub = new RemoteMemberCommunicatorHub();
    private final RemoteMemberCommunicatorSpy spy = new RemoteMemberCommunicatorSpy();

    @BeforeEach
    void setup() {
        communicatorHub.reset();
    }

    @Test
    void should_start_raft_server() {
        RaftGroup raftGroup = RaftGroup.create()
            .addMemberId(MemberId.create("server1"))
            .addMemberId(MemberId.create("server2"))
            .addMemberId(MemberId.create("server3"));
        FakeRemoteMemberCommunicator communicator = new FakeRemoteMemberCommunicator(communicatorHub, MemberId.create("server1"), spy);
        RaftServer raftServer = createDefaultRaftServer(communicator, raftGroup, RemoteMembers.REMOTE_MEMBER2, RemoteMembers.REMOTE_MEMBER3);

        raftServer.start();

        await()
            .atMost(1, TimeUnit.SECONDS)
            .until(raftServer::isStarted, is(true));

        raftServer.shutdown();
    }

    @Test
    void should_elected_leader_given_3_members() throws InterruptedException {
        RaftGroup raftGroup = RaftGroup.create()
            .addMemberId(MemberId.create("server1"))
            .addMemberId(MemberId.create("server2"))
            .addMemberId(MemberId.create("server3"));
        RaftServer raftServer1 = createRaftServer(createFakeRemoteMemberCommunicator(MemberId.create("server1")), raftGroup, 0, 3);
        RaftServer raftServer2 = createRaftServer(createFakeRemoteMemberCommunicator(MemberId.create("server2")), raftGroup, 1, 3);
        RaftServer raftServer3 = createRaftServer(createFakeRemoteMemberCommunicator(MemberId.create("server3")), raftGroup, 2, 3);

        raftServer2.start();
        raftServer3.start();
        raftServer1.start();

        MemberId leaderId = spy.expectLeader();
        assertThat(leaderId, notNullValue());

        raftServer1.shutdown();
        raftServer2.shutdown();
        raftServer3.shutdown();
    }

    @Test
    void should_elected_leader_given_5_members() throws InterruptedException {
        RaftGroup raftGroup = RaftGroup.create()
            .addMemberId(MemberId.create("server1"))
            .addMemberId(MemberId.create("server2"))
            .addMemberId(MemberId.create("server3"))
            .addMemberId(MemberId.create("server4"))
            .addMemberId(MemberId.create("server5"));
        RaftServer raftServer1 = createRaftServer(createFakeRemoteMemberCommunicator(MemberId.create("server1")), raftGroup, 0, 5);
        RaftServer raftServer2 = createRaftServer(createFakeRemoteMemberCommunicator(MemberId.create("server2")), raftGroup, 1, 5);
        RaftServer raftServer3 = createRaftServer(createFakeRemoteMemberCommunicator(MemberId.create("server3")), raftGroup, 2, 5);
        RaftServer raftServer4 = createRaftServer(createFakeRemoteMemberCommunicator(MemberId.create("server4")), raftGroup, 3, 5);
        RaftServer raftServer5 = createRaftServer(createFakeRemoteMemberCommunicator(MemberId.create("server5")), raftGroup, 4, 5);
        ImmutableMap<MemberId, RaftServer> servers = ImmutableMap.<MemberId, RaftServer>builder()
            .put(MemberId.create("server1"), raftServer1)
            .put(MemberId.create("server2"), raftServer2)
            .put(MemberId.create("server3"), raftServer3)
            .put(MemberId.create("server4"), raftServer4)
            .put(MemberId.create("server5"), raftServer5)
            .build();

        servers.values().forEach(RaftServer::start);

        MemberId leaderId = spy.expectLeader();
        assertThat(leaderId, notNullValue());
        servers.values().forEach(RaftServer::shutdown);
    }

    @Test
    void should_new_term_when_become_candicate() {
        RaftGroup raftGroup = RaftGroup.create()
            .addMemberId(MemberId.create("server1"))
            .addMemberId(MemberId.create("server2"))
            .addMemberId(MemberId.create("server3"));
        FakeRemoteMemberCommunicator communicator = new FakeRemoteMemberCommunicator(communicatorHub, MemberId.create("server1"), spy);
        RaftServer raftServer = createDefaultRaftServer(communicator, raftGroup, RemoteMembers.REMOTE_MEMBER2, RemoteMembers.REMOTE_MEMBER3);

        raftServer.start();

        await()
            .until(raftServer::currentTerm, is(Term.create(1)));

        raftServer.shutdown();
    }

    private static RaftServer createRaftServer(FakeRemoteMemberCommunicator communicator, RaftGroup raftGroup, int serverIdx, int size) {
        return createDefaultRaftServer(communicator, raftGroup, RemoteMembers.getRemoteMembers(serverIdx, size));
    }

    private FakeRemoteMemberCommunicator createFakeRemoteMemberCommunicator(MemberId memberId) {
        FakeRemoteMemberCommunicator communicator = new FakeRemoteMemberCommunicator(communicatorHub, memberId, spy);
        communicatorHub.registerCommunicator(memberId, communicator);
        return communicator;
    }

    private static RaftServer createDefaultRaftServer(FakeRemoteMemberCommunicator communicator, RaftGroup raftGroup, RemoteMember... remoteMembers) {
        RaftMemberDiscoveryStub raftMemberDiscovery = new RaftMemberDiscoveryStub();
        raftMemberDiscovery.addRemoteMember(remoteMembers);
        return RaftServer.builder()
            .localAddress(Address.of("127.0.0.1", 8080))
            .localMemberId(communicator.getMemberId())
            .raftConfig(RaftConfig.builder()
                .electionTimeout(500)
                .leaderHeartbeatInterval(100)
                .serverThreadPoolSize(3)
                .build())
            .raftMemberDiscovery(raftMemberDiscovery)
            .remoteMemberCommunicator(communicator)
            .raftGroup(raftGroup)
            .build();
    }
}
