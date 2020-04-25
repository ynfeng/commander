package com.github.ynfeng.commander.definition;

public class EndDefinitionBuilder implements Buildable {
    private final ProcessDefinition processDefinition;

    public EndDefinitionBuilder(ProcessDefinition processDefinition, ParentDefintion pre) {
        this.processDefinition = processDefinition;
        pre.next(EndDefinition.create());
    }

    @Override
    public ProcessDefinition build() {
        return processDefinition;
    }
}
