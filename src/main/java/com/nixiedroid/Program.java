package com.nixiedroid;


import com.nixiedroid.confg.ConfigStub;
import com.nixiedroid.confg.Config;
import com.nixiedroid.logger.Logger;
import com.nixiedroid.server.Server;
import com.nixiedroid.settings.ServerSettings;
import com.nixiedroid.settings.ServerSettingsStub;
import com.nixiedroid.sowftwareId.Generator;

public class Program {
    //Required valid ConfigStub
    public static Generator generator(){
        return config.getGenerator();
    }
    private static ServerSettingsStub settings = null;
    private static ConfigStub config = null;
    public static void start(){
        Server.start();
    }
    public static void stop() {
        if (config != null) Server.stop();
    }

    public static Config config(){
        return config;
    }
    public static ServerSettings settings() { return settings; }

    public static Logger log(){
        return config.logger();
    }

    public static void setConfig(ConfigStub config, ServerSettingsStub settings){
        if (Program.config == null) Program.config = config;
        if (Program.settings == null) Program.settings = settings;
    }
}