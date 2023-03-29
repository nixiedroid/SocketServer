package com.nixiedroid.logger;

import com.nixiedroid.Program;
import com.nixiedroid.settings.LogLevel;

public class LoggerStub implements Logger { //DELEGATION PATTERN
    private final Logger logger;

    public LoggerStub(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void err(String str) {
        if (Program.settings().getLevel() != LogLevel.NONE) logger.err(str);
    }

    @Override
    public void debug(String str) {
        if (Program.settings().getLevel() == LogLevel.DEBUG || Program.settings().getLevel() == LogLevel.VERBOSE) logger.debug(str);
    }

    @Override
    public void info(String str) {
        if (Program.settings().getLevel() != LogLevel.NONE && Program.settings().getLevel() != LogLevel.ERROR) logger.info(str);
    }

    @Override
    public void verbose(String str) {
        if (Program.settings().getLevel() == LogLevel.VERBOSE) logger.verbose(str);
    }
}
