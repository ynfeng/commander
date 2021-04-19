package com.github.ynfeng.commander.definition;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JoinDefinition)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        JoinDefinition that = (JoinDefinition) o;

        return ons.equals(that.ons);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ons.hashCode();
        return result;
    }
}
