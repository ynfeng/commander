package com.github.ynfeng.commander.definition;

public class ForkLink implements Link {
    private final String forkRefName;
    private final String[] refNames;

    public ForkLink(String forkRefName, String... refNames) {
        this.forkRefName = forkRefName;
        this.refNames = refNames;
    }

    @Override
    public void doLink(NodeDefinitions nodeDefinitions) {
        ForkDefinition forkDefinition = nodeDefinitions.get(forkRefName);
        for (String refName : refNames) {
            NodeDefinition node = nodeDefinitions.get(refName);
            forkDefinition.branch(node);
        }
    }
}
