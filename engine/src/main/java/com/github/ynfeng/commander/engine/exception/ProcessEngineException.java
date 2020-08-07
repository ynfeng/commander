package com.github.ynfeng.commander.engine.exception;

public class ProcessEngineException extends RuntimeException {
    public ProcessEngineException(String msg) {
        super(msg);
    }

    public ProcessEngineException(Throwable e) {
        super(e);
    }
}
