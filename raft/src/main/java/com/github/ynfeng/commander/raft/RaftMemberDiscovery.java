package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.Manageable;
import java.util.List;

public interface RaftMemberDiscovery extends Manageable {
    List<RemoteMember> remoteMembers(MemberId localMemberId);
}
