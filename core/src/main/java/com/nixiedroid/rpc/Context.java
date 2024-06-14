package com.nixiedroid.rpc;

import com.nixiedroid.rpc.dynamic.Loader;
import com.nixiedroid.rpc.dynamic.config.ConfigStub;
import com.nixiedroid.rpc.dynamic.logger.LogLevel;
import com.nixiedroid.rpc.dynamic.logger.Logger;
import com.nixiedroid.rpc.dynamic.settings.SettingsStub;
import com.nixiedroid.rpc.dynamic.sowftwareId.SoftwareIDGeneratorStub;

public class Context {


    private static final String CUSTOM_SID_GENERATOR = "sowftwareID.SoftwareIDGenerator";
    private static final String CUSTOM_OUTPUT_ROUTE = "logger.OutputRoute";
    private static final String CUSTOM_CONFIG = "Config";
    private static final String CUSTOM_SETTINGS = "Settings";

    private static SoftwareIDGeneratorStub softwareIDGenerator = null;
    private Logger logger = null;
    private LogLevel maxLogLevel;
    private static SettingsStub settings = null;
    private static ConfigStub config = null;


    private Context() {
        Loader l = new Loader();
        Context.config = (ConfigStub) l.apply("Config");
        Context.settings = (SettingsStub) l.apply("Settings");
        Context.softwareIDGenerator = (SoftwareIDGeneratorStub) l.apply("Settings");
        this.logger = (Logger) l.apply("Settings");
    }
    public static ConfigStub config(){
        return config;
    }
    public static SettingsStub settings() { return settings; }
    public static Logger l(){
       return    i().logger;
    }
    public static SoftwareIDGeneratorStub generator(){
        return softwareIDGenerator;
    }

    public static LogLevel level(){
        return i().maxLogLevel;
    }

    public static final Context i() {
        return Holder.getInstance();
    }

    private static class Holder {
        private static final Context INSTANCE = new Context();

        private static Context getInstance() {
            return INSTANCE;
        }
    }
}
