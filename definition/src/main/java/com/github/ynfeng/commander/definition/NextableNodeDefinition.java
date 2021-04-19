package com.github.ynfeng.commander.definition;

public abstract class NextableNodeDefinition extends AbstractNodeDefinition implements Nextable {
    private NodeDefinition next = NodeDefinition.NULL;

    protected NextableNodeDefinition(String refName) {
        super(refName);
    }

    @Override
    public void next(NodeDefinition nodeDefinition) {
        next = nodeDefinition;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends NodeDefinition> T next() {
        return (T) next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextableNodeDefinition)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        NextableNodeDefinition that = (NextableNodeDefinition) o;

        return next.equals(that.next);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + next.hashCode();
        return result;
    }
}
