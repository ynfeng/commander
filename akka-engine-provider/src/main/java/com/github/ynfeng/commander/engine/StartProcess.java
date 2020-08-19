package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.ProcessDefinition;

public class StartProcess implements EngineCommand {
    private final ProcessDefinition processDefinition;

    public StartProcess(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    public ProcessDefinition processDefinition() {
        return processDefinition;
    }
}
