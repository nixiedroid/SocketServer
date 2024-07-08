package com.nixiedroid.rpc;

import com.nixiedroid.rpc.dynamic.Dynamic;
import com.nixiedroid.rpc.dynamic.Loader;
import com.nixiedroid.rpc.dynamic.stubs.Config;
import com.nixiedroid.rpc.dynamic.stubs.OutputRoute;
import com.nixiedroid.rpc.dynamic.stubs.Settings;
import com.nixiedroid.rpc.dynamic.stubs.SoftwareIDGenerator;
import com.nixiedroid.rpc.util.logger.Level;
import com.nixiedroid.rpc.util.logger.Logger;

public class Context {

    private static Level maxLogLevel = Level.ALL;
    private static SoftwareIDGenerator softwareIDGenerator = null;
    private static Settings settings = null;
    private static Config config = null;

    static  {
        OutputRoute or = (OutputRoute) Loader.loadImplOrExample(Dynamic.LOGGER_ROUTE);
        Logger.Holder.init(or,Level.ALL);
        Context.config = (Config) Loader.loadImplOrExample(Dynamic.CONFIG);
        Context.settings = (Settings) Loader.loadImplOrExample(Dynamic.SETTINGS);
        Context.softwareIDGenerator = (SoftwareIDGenerator) Loader.loadImplOrExample(Dynamic.SID_GENERATOR);
        Context.maxLogLevel = settings().getLevel();
    }

    private Context(){
        throw new Error();
    }

    public static Config config() {
        return config;
    }

    public static Settings settings() {
        return settings;
    }

    public static SoftwareIDGenerator generator() {
        return softwareIDGenerator;
    }

    public static Level level() {
        return maxLogLevel;
    }
}
