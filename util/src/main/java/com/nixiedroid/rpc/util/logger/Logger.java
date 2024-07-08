package com.nixiedroid.rpc.util.logger;

import com.nixiedroid.rpc.dynamic.stubs.OutputRoute;

public class Logger {

    private final OutputRoute outputRoute;
    private final Level level;


    private Logger(OutputRoute route, Level level) {
        this.outputRoute = route;
        this.level = level;
    }

    public static void info(String s) {
        log(s, Level.INFO);
    }

    public static void trace(String s) {
        log(s, Level.TRACE);
    }

    public static void debug(String s) {
        log(s, Level.DEBUG);
    }

    public static void err(String s) {
        log(s, Level.ERROR);
    }

    private synchronized static void log(String s, Level level) {
        if (Holder.inst == null) return;
        if (isLogging(level)) {
            switch (level) {
                case DEBUG:
                    Holder.inst.outputRoute.debug(s);
                    break;
                case TRACE:
                    Holder.inst.outputRoute.trace(s);
                    break;
                case ERROR:
                    Holder.inst.outputRoute.err(s);
                    break;
                case INFO:
                    Holder.inst.outputRoute.debug(s);
                    break;
            }

        }
    }

    private static boolean isLogging(Level data) {
        return Holder.inst.level.getSeverity() <= data.getSeverity();
    }

    public static class Holder {
        private static volatile Logger inst;

        public static void init(OutputRoute route, Level level) {
            inst = new Logger(route, level);
        }
    }
}
