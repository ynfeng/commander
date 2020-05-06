package com.github.ynfeng.commander.core.definition;


import com.google.common.collect.Lists;
import java.util.List;

public class JoinDefinition extends NextableNodeDefinition {
    private final NodeDefinitions definitions;
    private final List<String> ons = Lists.newArrayList();

    protected JoinDefinition(String refName, NodeDefinitions definitions) {
        super(refName);
        this.definitions = definitions;
    }

    public void on(String... refNames) {
        for (String refName : refNames) {
            Nextable nextable = definitions.get(refName);
            nextable.next(this);
            ons.add(refName);
        }
    }
}
