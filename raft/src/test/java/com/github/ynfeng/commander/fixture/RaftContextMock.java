package com.github.ynfeng.commander.fixture;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.Term;
import java.util.List;

public class RaftContextMock implements RaftContext {
    private Term currentTerm;
    private MemberId localMermberId;

    @Override
    public MemberId localMermberId() {
        return localMermberId;
    }

    @Override
    public List<RemoteMember> remoteMembers() {
        return null;
    }

    @Override
    public RemoteMemberCommunicator remoteMemberCommunicator() {
        return null;
    }

    @Override
    public Term lastLogTerm() {
        return null;
    }

    @Override
    public long lastLogIndex() {
        return 0;
    }

    @Override
    public Term currentTerm() {
        return currentTerm;
    }

    @Override
    public void tryUpdateCurrentTerm(Term term) {

    }

    @Override
    public int quorum() {
        return 0;
    }

    @Override
    public void becomeLeader() {

    }

    @Override
    public long lastCommitIndex() {
        return 0;
    }

    @Override
    public long prevLogIndex() {
        return 0;
    }

    @Override
    public Term prevLogTerm() {
        return null;
    }

    @Override
    public void resetElectionTimer() {

    }

    public void setCurrentTerm(Term currentTerm) {
        this.currentTerm = currentTerm;
    }

    public void setLocalMemberId(MemberId localMemberId) {
        this.localMermberId = localMemberId;
    }
}
