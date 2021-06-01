package com.github.ynfeng.commander.support.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmderLoggerFactory {
    private CmderLoggerFactory() {
    }

    public static Logger getSystemLogger() {
        return LoggerFactory.getLogger("com.github.ynfeng.commander.system");
    }
}
