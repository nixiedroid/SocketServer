package com.nixiedroid.rpc;

import com.nixiedroid.rpc.dynamic.config.ConfigLoader;
import com.nixiedroid.rpc.dynamic.config.ConfigStub;
import com.nixiedroid.rpc.dynamic.logger.Logger;
import com.nixiedroid.rpc.dynamic.logger.OutputRouteLoader;
import com.nixiedroid.rpc.server.Server;
import com.nixiedroid.rpc.dynamic.settings.SettingsLoader;
import com.nixiedroid.rpc.dynamic.settings.SettingsStub;
import com.nixiedroid.rpc.dynamic.sowftwareId.SoftwareIDGeneratorStub;
import com.nixiedroid.rpc.dynamic.sowftwareId.SoftwareIDLoader;

public class Program {
    //Maybe reduce amount of byte[] allocations? There are over10000

    private static final String CUSTOM_SID_GENERATOR = "sowftwareID.SoftwareIDGenerator";
    private static final String CUSTOM_OUTPUT_ROUTE = "logger.OutputRoute";
    private static final String CUSTOM_CONFIG = "Config";
    private static final String CUSTOM_SETTINGS = "Settings";

    private static SoftwareIDGeneratorStub softwareIDGenerator = null;
    private static Logger logger = null;
    private static SettingsStub settings = null;
    private static ConfigStub config = null;
    private static final Server server = Server.getInstance();
    private static boolean isInitialised = false;

    public static ConfigStub config(){
        return config;
    }
    public static SettingsStub settings() { return settings; }
    public static Logger log(){
        return logger;
    }
    public static SoftwareIDGeneratorStub generator(){
        return softwareIDGenerator;
    }


    public static void main(String[] args) {
        loadDynamicModules();
        Program.start();
    }
    public static void init(){
        loadDynamicModules();
        Program.start();
    }

    public static void start(){
        server.startServerThread();
        isInitialised = true;
    }
    public static void stop() {
        if (isInitialised) {
            server.stop();
        }
    }

    private static void loadDynamicModules(){
        Program.config = ConfigLoader.load(CUSTOM_CONFIG);
        Program.settings = SettingsLoader.load(CUSTOM_SETTINGS);
        Program.softwareIDGenerator = SoftwareIDLoader.load(CUSTOM_SID_GENERATOR);
        Program.logger = new Logger(OutputRouteLoader.load(CUSTOM_OUTPUT_ROUTE));
    }
}