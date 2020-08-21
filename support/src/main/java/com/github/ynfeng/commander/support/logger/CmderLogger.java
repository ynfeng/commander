package com.github.ynfeng.commander.support.logger;

import org.slf4j.Logger;
import org.slf4j.Marker;

@SuppressWarnings("checkstyle:ParameterNumber")
public class CmderLogger implements Logger {
    private final Logger logger;

    public CmderLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String msg) {
        if (isTraceEnabled()) {
            logger.trace(msg);
        }
    }

    @Override
    public void trace(String format, Object arg) {
        if (isTraceEnabled()) {
            logger.trace(format, arg);
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if (isTraceEnabled()) {
            logger.trace(format, arg1, arg2);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (isTraceEnabled()) {
            logger.trace(format, arguments);
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (isTraceEnabled()) {
            logger.trace(msg, t);
        }
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(Marker marker, String msg) {
        if (isTraceEnabled()) {
            logger.trace(marker, msg);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        if (isTraceEnabled()) {
            logger.trace(marker, format, arg);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        if (isTraceEnabled()) {
            logger.trace(marker, format, arg1, arg2);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        if (isTraceEnabled()) {
            logger.trace(marker, format, argArray);
        }
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        if (isTraceEnabled()) {
            logger.trace(marker, msg, t);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        if (isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (isDebugEnabled()) {
            logger.debug(format, arg);
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if (isDebugEnabled()) {
            logger.debug(format, arg1, arg2);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            logger.debug(format, arguments);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (isDebugEnabled()) {
            logger.debug(msg, t);
        }
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(Marker marker, String msg) {
        if (isDebugEnabled()) {
            logger.debug(marker, msg);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        if (isErrorEnabled()) {
            logger.debug(marker, format, arg);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        if (isDebugEnabled()) {
            logger.debug(marker, format, arg1, arg2);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void info(String msg) {

    }

    @Override
    public void info(String format, Object arg) {

    }

    @Override
    public void info(String format, Object arg1, Object arg2) {

    }

    @Override
    public void info(String format, Object... arguments) {
        if (logger.isInfoEnabled()) {
            logger.info(format, arguments);
        }
    }

    @Override
    public void info(String msg, Throwable t) {

    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void info(Marker marker, String msg) {

    }

    @Override
    public void info(Marker marker, String format, Object arg) {

    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {

    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public void warn(String msg) {

    }

    @Override
    public void warn(String format, Object arg) {

    }

    @Override
    public void warn(String format, Object... arguments) {

    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {

    }

    @Override
    public void warn(String msg, Throwable t) {

    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    @Override
    public void warn(Marker marker, String msg) {

    }

    @Override
    public void warn(Marker marker, String format, Object arg) {

    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {

    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void error(String msg) {

    }

    @Override
    public void error(String format, Object arg) {

    }

    @Override
    public void error(String format, Object arg1, Object arg2) {

    }

    @Override
    public void error(String format, Object... arguments) {
        if (logger.isErrorEnabled()) {
            logger.error(format, arguments);
        }
    }

    @Override
    public void error(String msg, Throwable t) {

    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public void error(Marker marker, String msg) {

    }

    @Override
    public void error(Marker marker, String format, Object arg) {

    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {

    }
}
