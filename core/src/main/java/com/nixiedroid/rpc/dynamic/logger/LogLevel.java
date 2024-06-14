package com.nixiedroid.rpc.dynamic.logger;

public enum LogLevel {
    DEBUG(0) ,
    VERBOSE(1) ,
    INFO(2) ,
    ERROR(3) ,
    NONE(10) ;
    private int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public static boolean isLogging(LogLevel ctx, LogLevel level){
        return ctx.priority <= level.priority;
    }
}
