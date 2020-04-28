package com.github.ynfeng.commander.definition;

public interface NodeDefinition {
    NodeDefinition NULL = new NodeDefinition() {
        @Override
        public void next(NodeDefinition nodeDefinition) {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unchecked")
        @Override
        public NodeDefinition next() {
            return null;
        }
    };

    <T extends NodeDefinition> T next();

    void next(NodeDefinition nodeDefinition);
}
