package com.github.ynfeng.commander.definition;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;

public class NodeDefinitions {
    private final Map<String, NodeDefinition> definitions = Maps.newHashMap();

    private NodeDefinitions() {
    }

    public static NodeDefinitions create() {
        return new NodeDefinitions();
    }

    public void add(NodeDefinition nodeDefinition) {
        if (definitions.containsKey(nodeDefinition.refName())) {
            throw new ProcessDefinitionException(String.format("The ref name \"%s\" was duplicated",
                nodeDefinition.refName()));
        }
        definitions.put(nodeDefinition.refName(), nodeDefinition);
    }

    @SuppressWarnings("unchecked")
    public <T extends NodeDefinition> T get(String refName) {
        try {
            return (T) Preconditions.checkNotNull(definitions.get(refName));
        } catch (Exception e) {
            throw new ProcessDefinitionException(String.format("The \"%s\" node definition not exists.", refName));
        }
    }
}
