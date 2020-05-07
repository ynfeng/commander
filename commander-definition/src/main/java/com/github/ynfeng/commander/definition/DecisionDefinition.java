package com.github.ynfeng.commander.definition;

import com.github.ynfeng.commander.core.definition.AbstractNodeDefinition;
import com.github.ynfeng.commander.core.definition.NodeDefinition;

public class DecisionDefinition extends AbstractNodeDefinition {
    private final ConditionBranches branches = new ConditionBranches();

    protected DecisionDefinition(String refName) {
        super(refName);
    }

    public DecisionDefinition condition(Expression expression, NodeDefinition nodeDefinition) {
        branches.add(expression, nodeDefinition);
        return this;
    }

    public ConditionBranches branches() {
        return branches;
    }
}
