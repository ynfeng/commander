package com.github.ynfeng.commander.raft;

import java.util.List;

public interface RaftContext {
    void becomeCandidate();

    MemberId localMermberId();

    List<RemoteMember> remoteMembers();

    RemoteMemberCommunicator remoteMemberCommunicator();

    Term lastLogTerm();

    long lastLogIndex();

    Term currentTerm();

    void tryUpdateCurrentTerm(Term term);

    int quorum();

    void becomeLeader();

    long lastCommitIndex();

    long prevLogIndex();

    Term prevLogTerm();

    void resetElectionTimer();

    void becomeFollower(Term term, MemberId leaderId);

    void pauseElectionTimer();

    void resumeElectionTimer();

    void setLeader(MemberId leaderId);

    VoteTracker voteTracker();

    void nextTerm();

    MemberId currentLeader();
}
