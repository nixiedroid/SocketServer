package com.nixiedroid.dynamic.logger;

import com.nixiedroid.Program;

public class Logger {

    private final OutputRouteStub outputRoute;
    public void err(String str) {
        if (Program.settings().getLevel() != LogLevel.NONE) {
            outputRoute.err(str);
        }
    }


    public void debug(String str) {
        if (Program.settings().getLevel() == LogLevel.DEBUG
                || Program.settings().getLevel() == LogLevel.VERBOSE) {
            outputRoute.debug(str);
        }
    }


    public void info(String str) {
        if (Program.settings().getLevel() != LogLevel.NONE
                && Program.settings().getLevel() != LogLevel.ERROR){
            outputRoute.info(str);
        }
    }


    public void verbose(String str) {
        if (Program.settings().getLevel() == LogLevel.VERBOSE) {
            outputRoute.verbose(str);
        }
    }

    public Logger(OutputRouteStub route){
        outputRoute = route;
    }
}
