package com.nixiedroid.logger;

public interface Logger {
    void err(String str);
    void debug(String str);
    void info(String str);
    void verbose(String str);
}
