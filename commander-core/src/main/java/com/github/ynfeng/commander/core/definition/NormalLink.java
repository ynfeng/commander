package com.github.ynfeng.commander.core.definition;

public class NormalLink implements Link {
    private final String from;
    private final String to;

    public NormalLink(String fromRefName, String toRefName) {
        from = fromRefName;
        to = toRefName;
    }

    @Override
    public void doLink(NodeDefinitions nodeDefinitions) {
        NextableNodeDefinition start = nodeDefinitions.get(from);
        NodeDefinition next = nodeDefinitions.get(to);
        start.next(next);
    }
}
