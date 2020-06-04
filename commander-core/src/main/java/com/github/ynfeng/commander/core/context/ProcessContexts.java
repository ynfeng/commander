package com.github.ynfeng.commander.core.context;

public interface ProcessContexts {
    void add(ProcessContext processContext);

    int size();

    void remove(ProcessContext processContext);
}
