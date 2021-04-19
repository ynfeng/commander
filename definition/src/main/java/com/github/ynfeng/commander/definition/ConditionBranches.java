package com.github.ynfeng.commander.definition;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class ConditionBranches {
    private final List<ConditionBranch> branches = Lists.newArrayList();

    protected ConditionBranches() {
    }

    public Iterator<ConditionBranch> iterator() {
        return branches.iterator();
    }

    public int size() {
        return branches.size();
    }

    public void add(Expression expression, NodeDefinition nodeDefinition) {
        branches.add(new ConditionBranch(expression, nodeDefinition));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConditionBranches)) {
            return false;
        }

        ConditionBranches that = (ConditionBranches) o;

        return branches.equals(that.branches);
    }

    @Override
    public int hashCode() {
        return branches.hashCode();
    }
}
