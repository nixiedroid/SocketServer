package com.nixiedroid.logger;

import com.nixiedroid.Program;
import com.nixiedroid.settings.LogLevel;
import com.nixiedroid.util.Date;

public class SoutLoggerImpl implements Logger{
    @Override
    public void err(String str) {
        if (Program.settings().getLevel() != LogLevel.NONE) System.err.println(Date.getDate() + " - [ERR] - " + str);
    }

    @Override
    public void debug(String str) {
        if (Program.settings().getLevel() == LogLevel.DEBUG || Program.settings().getLevel() == LogLevel.VERBOSE)
            System.out.println(Date.getDate() + " - [DEBUG] - " + str);
    }

    @Override
    public void info(String str) {
        if (Program.settings().getLevel() != LogLevel.NONE && Program.settings().getLevel() != LogLevel.ERROR)
            System.out.println(Date.getDate() + " - [INFO] - " + str);
    }

    @Override
    public void verbose(String str) {
            if (Program.settings().getLevel() == LogLevel.VERBOSE) System.out.println(Date.getDate() + " - [VERBOSE] - " + str);
    }
}
