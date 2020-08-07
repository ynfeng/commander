package com.github.ynfeng.commander.core.definition;

import java.util.Arrays;

public class JoinLink implements Link {
    private final String joinRefName;
    private final String[] refNames;

    public JoinLink(String joinRefName, String... refNames) {
        this.joinRefName = joinRefName;
        this.refNames = refNames;
    }

    @Override
    public void doLink(NodeDefinitions nodeDefinitions) {
        JoinDefinition joinDefinition = nodeDefinitions.get(joinRefName);
        NextableNodeDefinition[] ons = Arrays.stream(refNames).map(nodeDefinitions::get)
            .toArray(size -> new NextableNodeDefinition[size]);
        joinDefinition.on(ons);
    }
}
