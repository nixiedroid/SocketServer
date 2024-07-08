package com.nixiedroid.rpc.dynamic.impl;

import com.nixiedroid.rpc.util.logger.Level;
import com.nixiedroid.rpc.dynamic.stubs.Settings;


public class SettingsExample extends Settings {

    private int PORT = 8080;
    private Level LOGLEVEL = Level.DEBUG;

    @Override
    public Level getLevel() {
        return LOGLEVEL;
    }

    @Override
    public void setLevel(Level level) {
        this.LOGLEVEL = level;
    }

    @Override
    public int getServerPort() {
        return PORT;
    }


    @Override
    public void setPort(int port) {
        this.PORT = port;
    }


}
