package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface RaftContext {
    void becomeCandidate();

    MemberId localMermberId();

    List<RemoteMember> remoteMembers();

    Term lastLogTerm();

    long lastLogIndex();

    Term currentTerm();

    int quorum();

    void becomeLeader();

    long lastCommitIndex();

    long prevLogIndex();

    Term prevLogTerm();

    void resetElectionTimer();

    void becomeFollower(Term term, MemberId leaderId);

    void pauseElectionTimer();

    void resumeElectionTimer();

    VoteTracker voteTracker();

    void nextTerm();

    MemberId currentLeader();

    boolean isLeader();

    <R extends Response> CompletableFuture<R> sendRequest(RemoteMember remoteMember, Request request);
}
