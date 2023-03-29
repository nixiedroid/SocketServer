package com.nixiedroid;


import com.nixiedroid.confg.ConfigStub;
import com.nixiedroid.confg.Config;
import com.nixiedroid.logger.Logger;
import com.nixiedroid.server.Server;
import com.nixiedroid.settings.ServerSettings;
import com.nixiedroid.settings.ServerSettingsStub;
import com.nixiedroid.sowftwareId.AbstractGenerator;
public class Program {
    //Program holds config and Program is main target from starting outside
    //Required valid ConfigStub
    public static AbstractGenerator generator(){
        return settings.getGenerator();
    }
    private static ServerSettingsStub settings = null;
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

    public static Config config(){
        return config;
    }
    public static ServerSettings settings() { return settings; }
    public static Logger log(){
        return settings.logger();
    }

    public static void setConfig(ConfigStub config, ServerSettingsStub settings){
        if (Program.config == null) Program.config = config;
        if (Program.settings == null) Program.settings = settings;
    }
}