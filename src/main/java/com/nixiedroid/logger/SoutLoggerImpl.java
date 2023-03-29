package com.nixiedroid.logger;

import com.nixiedroid.Program;
import com.nixiedroid.settings.LogLevel;
import com.nixiedroid.util.Date;

public class SoutLoggerImpl implements Logger{
    @Override
    public void err(String str) {
         System.err.println(Date.getDate() + " - [ERR] - " + str);
    }

    @Override
    public void debug(String str) {
            System.out.println(Date.getDate() + " - [DEBUG] - " + str);
    }

    @Override
    public void info(String str) {
            System.out.println(Date.getDate() + " - [INFO] - " + str);
    }

    @Override
    public void verbose(String str) {
            System.out.println(Date.getDate() + " - [VERBOSE] - " + str);
    }
}
