package com.github.ynfeng.commander.definition;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessDefinitionBuilder {
    public static StartDefinitionBuilder create(String name, int version) {
        ProcessDefinition processDefinition = new ProcessDefinition(name, version);
        return new StartDefinitionBuilder(processDefinition);
    }
}
