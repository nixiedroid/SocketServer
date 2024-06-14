package com.nixiedroid.rpc.dynamic.logger;

import com.nixiedroid.rpc.Context;

public class Logger {

    private final OutputRouteStub outputRoute;

    public void info(String s){
        if (isLogging(Context.level(),LogLevel.INFO)) outputRoute.info(s);
    }
    public void verbose(String s){
        if (isLogging(Context.level(),LogLevel.VERBOSE)) outputRoute.verbose(s);
    }
    public void debug(String s){
        if (isLogging(Context.level(),LogLevel.DEBUG)) outputRoute.debug(s);
    }
    public void err(String s){
        if (isLogging(Context.level(),LogLevel.ERROR)) outputRoute.err(s);
    }
    public void log(String s, LogLevel level){
        if (isLogging(Context.level(),level)) {
            switch (level){
                case DEBUG:
                    outputRoute.debug(s);
                    break;
                case VERBOSE:
                    outputRoute.verbose(s);
                    break;
                case ERROR:
                    outputRoute.err(s);
                    break;
                case INFO:
                    outputRoute.debug(s);
                    break;
            }

        }
    }
    private boolean isLogging(LogLevel current, LogLevel data){
        return LogLevel.isLogging(current,data);
    }

    public Logger(OutputRouteStub route){
        outputRoute = route;
    }
}
