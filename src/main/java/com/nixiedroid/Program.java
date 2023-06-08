package com.nixiedroid;


import com.nixiedroid.confg.ConfigLoader;
import com.nixiedroid.confg.ConfigStub;
import com.nixiedroid.logger.Logger;
import com.nixiedroid.logger.OutputRouteLoader;
import com.nixiedroid.server.Server;
import com.nixiedroid.settings.SettingsLoader;
import com.nixiedroid.settings.SettingsStub;
import com.nixiedroid.sowftwareId.SoftwareIDGeneratorStub;
import com.nixiedroid.sowftwareId.SoftwareIDLoader;

public class Program {
    //Maybe reduce amount of byte[] allocations? There are over10000

    private static final String CUSTOM_SID_GENERATOR = "com.nixiedroid.custom.sowftwareID.SoftwareIDGenerator";
    private static final String CUSTOM_OUTPUT_ROUTE = "com.nixiedroid.custom.logger.OutputRoute";
    private static final String CUSTOM_CONFIG = "com.nixiedroid.custom.Config";
    private static final String CUSTOM_SETTINGS = "com.nixiedroid.custom.Settings";


    private static SoftwareIDGeneratorStub softwareIDGenerator = null;
    private static Logger logger = null;
    private static SettingsStub settings = null;
    private static ConfigStub config = null;
    private static final Server server = Server.getInstance();
    private static boolean isCreated = false;
    public static void start(){
        server.start();
        isCreated = true;
    }
    public static void stop() {
        if (isCreated) server.stop();
    }

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

    public static void setConfig(ConfigStub config, SettingsStub settings){
        if (Program.config == null) Program.config = config;
        if (Program.settings == null) Program.settings = settings;
    }
    public static void setSoftwareIDGenerator(SoftwareIDGeneratorStub generator){
        softwareIDGenerator = generator;
    }
    public static void setLogger (Logger logger){
        Program.logger = logger;
    }


    public static void main(String[] args) {
        ConfigStub config = ConfigLoader.load(CUSTOM_CONFIG);
        SettingsStub settings = SettingsLoader.load(CUSTOM_SETTINGS);
        Program.setConfig(config,settings);
        Program.setSoftwareIDGenerator(SoftwareIDLoader.load(CUSTOM_SID_GENERATOR));
        Logger logger = new Logger(OutputRouteLoader.load(CUSTOM_OUTPUT_ROUTE));
        Program.setLogger(logger);
        Program.start();
        InputReader reader = new InputReader();
    }
}