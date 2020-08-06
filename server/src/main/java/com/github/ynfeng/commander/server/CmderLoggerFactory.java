package com.github.ynfeng.commander.server;

import org.slf4j.LoggerFactory;

public class CmderLoggerFactory {
    private CmderLoggerFactory() {
    }

    public static CmderLogger getServerLogger() {
        return new CmderLogger(LoggerFactory.getLogger("com.github.ynfeng.commander.server"));
    }
}
