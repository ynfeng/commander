package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.support.Manageable;

public interface ProcessEngine extends Manageable {

    ProcessFuture startProcess(String name, int version, Variables variables);

    ProcessFuture startProcess(String name, int version);

    ContinueFuture continueProcess(ProcessId processId, String nodeRefName, Variables variables);
}
