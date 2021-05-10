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
    private int lastLogIndex = 0;
    private Term lastLogTerm = Term.create(0);

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
        return lastLogTerm;
    }

    @Override
    public long lastLogIndex() {
        return lastLogIndex;
    }

    @Override
    public Term currentTerm() {
        return currentTerm;
    }

    @Override
    public void tryUpdateCurrentTerm(Term term) {
        currentTerm = term;
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

    public void setLastLogIndex(int lastLogIndex) {
        this.lastLogIndex = lastLogIndex;
    }

    public void setLastLogTerm(Term lastLogTerm) {
        this.lastLogTerm = lastLogTerm;
    }
}
