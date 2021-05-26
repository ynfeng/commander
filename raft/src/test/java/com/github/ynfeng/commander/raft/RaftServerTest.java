package com.github.ynfeng.commander.raft;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;

import com.github.ynfeng.commander.fixture.FakeRemoteMemberCommunicator;
import com.github.ynfeng.commander.fixture.RaftMemberDiscoveryStub;
import com.github.ynfeng.commander.fixture.RemoteMemberCommunicatorHub;
import com.github.ynfeng.commander.fixture.RemoteMemberCommunicatorSpy;
import com.github.ynfeng.commander.support.Address;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class RaftServerTest {
    private static final RemoteMember REMOTE_MEMBER1 = RemoteMember.create(MemberId.create("server1"), Address.of("127.0.0.1", 8081));
    private static final RemoteMember REMOTE_MEMBER2 = RemoteMember.create(MemberId.create("server2"), Address.of("127.0.0.1", 8082));
    private static final RemoteMember REMOTE_MEMBER3 = RemoteMember.create(MemberId.create("server3"), Address.of("127.0.0.1", 8083));
    private static final RemoteMember REMOTE_MEMBER4 = RemoteMember.create(MemberId.create("server4"), Address.of("127.0.0.1", 8084));
    private static final RemoteMember REMOTE_MEMBER5 = RemoteMember.create(MemberId.create("server5"), Address.of("127.0.0.1", 8085));
    private static final RemoteMember[] REMOTE_MEMBERS = new RemoteMember[] {REMOTE_MEMBER1, REMOTE_MEMBER2, REMOTE_MEMBER3, REMOTE_MEMBER4, REMOTE_MEMBER5};
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
        RaftServer raftServer = createDefaultRaftServer(communicator, raftGroup, REMOTE_MEMBER2, REMOTE_MEMBER3);

        raftServer.start();

        await()
            .atMost(1, TimeUnit.SECONDS)
            .until(raftServer::isStarted, is(true));
    }

    @Test
    @Disabled
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

        Thread.sleep(1000 * 10);
    }

    @Test
    @Disabled
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

        raftServer5.start();
        raftServer3.start();
        raftServer2.start();
        raftServer1.start();
        raftServer4.start();

        Thread.sleep(1000 * 10);
        System.out.println();
        System.out.println("shutdown server");
        raftServer1.shutdown();
        Thread.sleep(1000 * 60);
    }

    @Test
    void should_new_term_when_become_candicate() {
        RaftGroup raftGroup = RaftGroup.create()
            .addMemberId(MemberId.create("server1"))
            .addMemberId(MemberId.create("server2"))
            .addMemberId(MemberId.create("server3"));
        FakeRemoteMemberCommunicator communicator = new FakeRemoteMemberCommunicator(communicatorHub, MemberId.create("server1"), spy);
        RaftServer raftServer = createDefaultRaftServer(communicator, raftGroup, REMOTE_MEMBER2, REMOTE_MEMBER3);

        raftServer.start();

        await()
            .until(raftServer::currentTerm, is(Term.create(1)));
    }

    private static RemoteMember[] getRemoteMembers(int currentMemberIdx, int size) {
        RemoteMember[] result = new RemoteMember[size - 1];
        int resultIdx = 0;
        int i = 0;
        while (resultIdx < result.length) {
            if (currentMemberIdx == i) {
                i++;
                continue;
            }
            result[resultIdx++] = REMOTE_MEMBERS[i++];
        }
        return result;
    }

    private static RaftServer createRaftServer(FakeRemoteMemberCommunicator communicator, RaftGroup raftGroup, int serverIdx, int size) {
        return createDefaultRaftServer(communicator, raftGroup, getRemoteMembers(serverIdx, size));
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
