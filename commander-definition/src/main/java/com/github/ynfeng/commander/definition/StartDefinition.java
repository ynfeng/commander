package com.github.ynfeng.commander.definition;

public class StartDefinition extends AbstractNodeDefinition {
    private NodeDefinition next;

    public StartDefinition() {
        super("start");
    }

    @Override
    public void next(NodeDefinition nodeDefinition) {
        next = nodeDefinition;
    }

    @Override
    public <T extends NodeDefinition> T next() {
        return (T) next;
    }
}
