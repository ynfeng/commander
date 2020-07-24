package com.github.ynfeng.commander.core.definition;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class JoinDefinition extends NextableNodeDefinition {
    private final List<String> ons = Lists.newArrayList();

    public JoinDefinition(String refName) {
        super(refName);
    }

    public void on(NextableNodeDefinition... nodes) {
        for (NextableNodeDefinition node : nodes) {
            node.next(this);
            ons.add(node.refName());
        }
    }

    public List<String> ons() {
        return Collections.unmodifiableList(ons);
    }
}
