package com.github.ynfeng.commander.engine;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class ProcessResult {
    private final List<String> executedNodes = Lists.newArrayList();
    private ProcessResult() {

    }

    public static ProcessResultBuilder builder() {
        return new ProcessResultBuilder();
    }

    public List<String> getExecutedNodes() {
        return Collections.unmodifiableList(executedNodes);
    }

    public static class ProcessResultBuilder {
        private ProcessResult result = new ProcessResult();

        private ProcessResultBuilder() {

        }

        public ProcessResultBuilder executedNodes(List<String> executedNodes) {
            result.executedNodes.addAll(executedNodes);
            return this;
        }

        public ProcessResult build() {
            return result;
        }
    }
}
