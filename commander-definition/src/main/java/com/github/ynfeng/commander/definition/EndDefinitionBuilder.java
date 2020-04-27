package com.github.ynfeng.commander.definition;

public class EndDefinitionBuilder implements Buildable {
    protected EndDefinitionBuilder(ParentDefintion pre) {
        pre.next(EndDefinition.create());
    }

    @Override
    public ProcessDefinition build() {
        return ProcessDefinitionBuilderContext.processDefinition();
    }
}
