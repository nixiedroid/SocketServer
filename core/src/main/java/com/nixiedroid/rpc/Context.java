package com.nixiedroid.rpc;

import com.nixiedroid.rpc.dynamic.config.ConfigLoader;
import com.nixiedroid.rpc.dynamic.config.ConfigStub;
import com.nixiedroid.rpc.dynamic.logger.Logger;
import com.nixiedroid.rpc.dynamic.logger.OutputRouteLoader;
import com.nixiedroid.rpc.dynamic.settings.SettingsLoader;
import com.nixiedroid.rpc.dynamic.settings.SettingsStub;
import com.nixiedroid.rpc.dynamic.sowftwareId.SoftwareIDGeneratorStub;
import com.nixiedroid.rpc.dynamic.sowftwareId.SoftwareIDLoader;

public class Context {


    private static final String CUSTOM_SID_GENERATOR = "sowftwareID.SoftwareIDGenerator";
    private static final String CUSTOM_OUTPUT_ROUTE = "logger.OutputRoute";
    private static final String CUSTOM_CONFIG = "Config";
    private static final String CUSTOM_SETTINGS = "Settings";

    private static SoftwareIDGeneratorStub softwareIDGenerator = null;
    private Logger logger = null;
    private int maxLogLevel;
    private static SettingsStub settings = null;
    private static ConfigStub config = null;


    private Context() {
        Context.config = ConfigLoader.load(CUSTOM_CONFIG);
        Context.settings = SettingsLoader.load(CUSTOM_SETTINGS);
        Context.softwareIDGenerator = SoftwareIDLoader.load(CUSTOM_SID_GENERATOR);
        this.logger = new Logger(OutputRouteLoader.load(CUSTOM_OUTPUT_ROUTE));
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

    public static int level(){
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
