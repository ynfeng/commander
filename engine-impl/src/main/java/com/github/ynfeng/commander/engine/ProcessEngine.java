package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.ProcessDefinition;

public interface ProcessEngine {
    void startup();

    void startProcess(ProcessDefinition processDefinition);
}
