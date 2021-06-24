package com.github.ynfeng.commander.fixture;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.support.Address;

public class RemoteMembers {
    public static final RemoteMember REMOTE_MEMBER1 = RemoteMember.create(MemberId.create("server1"), Address.of("127.0.0.1", 8111));
    public static final RemoteMember REMOTE_MEMBER2 = RemoteMember.create(MemberId.create("server2"), Address.of("127.0.0.1", 8112));
    public static final RemoteMember REMOTE_MEMBER3 = RemoteMember.create(MemberId.create("server3"), Address.of("127.0.0.1", 8113));
    public static final RemoteMember REMOTE_MEMBER4 = RemoteMember.create(MemberId.create("server4"), Address.of("127.0.0.1", 8114));
    public static final RemoteMember REMOTE_MEMBER5 = RemoteMember.create(MemberId.create("server5"), Address.of("127.0.0.1", 8115));
    public static final RemoteMember[] REMOTE_MEMBERS = new RemoteMember[] {REMOTE_MEMBER1, REMOTE_MEMBER2, REMOTE_MEMBER3, REMOTE_MEMBER4, REMOTE_MEMBER5};

    public static RemoteMember[] getRemoteMembers(int currentMemberIdx, int size) {
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
}
