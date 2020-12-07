package com.github.ynfeng.commander.definition;

import com.google.common.collect.Lists;
import java.util.Arrays;

public class ProcessDefinitionBuilder {
    private final NodeDefinitions nodeDefinitions = NodeDefinitions.create();
    private ProcessDefinition processDefinition;
    private String name;
    private int version;
    private RelationShips relationShips = new RelationShips(Lists.newArrayList());

    protected ProcessDefinitionBuilder() {
    }

    public ProcessDefinitionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProcessDefinitionBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public ProcessDefinitionBuilder withNodes(NodeDefinition... nodes) {
        Arrays.stream(nodes).forEach(nodeDefinitions::add);
        return this;
    }

    public ProcessDefinitionBuilder withRelationShips(RelationShips relationShips) {
        this.relationShips = relationShips;
        return this;
    }

    public ProcessDefinition build() {
        processDefinition = new ProcessDefinition(name, version);
        relationShips.links().stream()
            .forEach(each -> each.doLink(nodeDefinitions));
        processDefinition.firstNode(nodeDefinitions.get("start"));
        return processDefinition;
    }
}
