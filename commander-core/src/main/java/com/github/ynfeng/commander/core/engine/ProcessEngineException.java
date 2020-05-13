package com.github.ynfeng.commander.core.engine;

public class ProcessEngineException extends RuntimeException {
    public ProcessEngineException(String msg) {
        super(msg);
    }

    public ProcessEngineException(Exception e) {
        super(e);
    }
}
