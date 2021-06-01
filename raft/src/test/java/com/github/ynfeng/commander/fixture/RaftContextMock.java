package com.github.ynfeng.commander.fixture;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.VoteTracker;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RaftContextMock implements RaftContext {
    private Term currentTerm;
    private MemberId localMermberId;
    private int lastLogIndex;
    private Term lastLogTerm = Term.create(0);
    private boolean becomeCandidateCalled;
    private boolean becomeLeaderCalled;
    private VoteTracker voteTracker = new VoteTracker();
    private boolean becomeFollowerCalled;
    private boolean electionTimerReseted;
    private MemberId leaderId;

    public void setVoteTracker(VoteTracker voteTracker) {
        this.voteTracker = voteTracker;
    }

    @Override
    public void becomeCandidate() {
        becomeCandidateCalled = true;
    }

    @Override
    public MemberId localMermberId() {
        return localMermberId;
    }

    @Override
    public List<RemoteMember> remoteMembers() {
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
    public int quorum() {
        return 0;
    }

    @Override
    public void becomeLeader() {
        becomeCandidateCalled = true;
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
        electionTimerReseted = true;
    }

    @Override
    public void becomeFollower(Term term, MemberId leaderId) {
        becomeFollowerCalled = true;
    }

    @Override
    public void pauseElectionTimer() {

    }

    @Override
    public void resumeElectionTimer() {
    }

    @Override
    public VoteTracker voteTracker() {
        return voteTracker;
    }

    @Override
    public void nextTerm() {

    }

    @Override
    public MemberId currentLeader() {
        return leaderId;
    }

    @Override
    public boolean isLeader() {
        return false;
    }

    @Override
    public <R extends Response> CompletableFuture<R> sendRequest(RemoteMember remoteMember, Request request) {
        return null;
    }

    public void setCurrentTerm(Term currentTerm) {
        this.currentTerm = currentTerm;
    }

    public void setLocalMemberId(MemberId localMemberId) {
        localMermberId = localMemberId;
    }

    public void setLastLogIndex(int lastLogIndex) {
        this.lastLogIndex = lastLogIndex;
    }

    public void setLastLogTerm(Term lastLogTerm) {
        this.lastLogTerm = lastLogTerm;
    }

    public boolean calledBecomeCandidate() {
        return becomeCandidateCalled;
    }

    public boolean calledBecomeLeader() {
        return becomeLeaderCalled;
    }

    public boolean calledBecomeFollower() {
        return becomeFollowerCalled;
    }

    public Term calledUpdateTerm() {
        return currentTerm;
    }

    public boolean calledResetElectionTimer() {
        return electionTimerReseted;
    }

    public void setCurrentLeader(MemberId leaderId) {
        this.leaderId = leaderId;
    }
}
