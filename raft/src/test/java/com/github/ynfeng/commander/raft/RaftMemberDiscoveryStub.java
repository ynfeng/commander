package com.github.ynfeng.commander.raft;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;

public class RaftMemberDiscoveryStub implements RaftMemberDiscovery {
    private final List<RemoteMember> remoteMembers = Lists.newArrayList();

    public void addRemoteMember(RemoteMember... remoteMember) {
        remoteMembers.addAll(Arrays.asList(remoteMember));
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public List<RemoteMember> remoteMembers(MemberId localMemberId) {
        return remoteMembers;
    }
}
