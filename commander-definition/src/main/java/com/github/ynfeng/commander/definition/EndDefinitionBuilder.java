package com.github.ynfeng.commander.definition;

public class EndDefinitionBuilder implements Buildable {
    protected EndDefinitionBuilder(NodeDefinition pre) {
        pre.next(EndDefinition.create());
    }

    @Override
    public ProcessDefinition build() {
        return ProcessDefinitionBuilderContext.processDefinition();
    }
}
