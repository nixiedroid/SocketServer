package com.nixiedroid.logger;

public class LoggerStub implements Logger { //DELEGATION PATTERN
    private final Logger logger;
    @Override
    public void err(String str) {
        logger.err(str);
    }

    @Override
    public void debug(String str) {
        logger.debug(str);
    }

    @Override
    public void info(String str) {
        logger.info(str);
    }

    @Override
    public void verbose(String str) {
        logger.verbose(str);
    }

    public LoggerStub(Logger logger) {
        this.logger = logger;
    }
}
