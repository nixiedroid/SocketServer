package com.nixiedroid.settings;

public class ServerSettingsExample implements ServerSettings {
    private static final int PORT = 8080;
    private final LogLevel LOGLEVEL = LogLevel.DEBUG;
    private static final int PING_TIME = 0x42; //MINUTES
    private static final int DELAY_TIME = 0x4242; //MINUTES
    private static final int CLIENT_COUNT = 25;
    private static final String HARDWARE_ID = "BAAAAAAAAAAAAAAD";
    private static final int LANG_CODE = 1033;
    private static final String SOFTWARE_ID = null;

    @Override
    public LogLevel getLevel() {
        return LOGLEVEL;
    }


    @Override
    public int getServerPort() {
        return PORT;
    }

    @Override
    public int getPingTime() {
        return PING_TIME;
    }

    @Override
    public int getDelayTime() {
        return DELAY_TIME;
    }

    @Override
    public int getMinClientCount() {
        return CLIENT_COUNT;
    }

    @Override
    public String getHardwareID() {
        return HARDWARE_ID;
    }

    @Override
    public int getLang() {
        return LANG_CODE;
    }

    @Override
    public String getSoftwareID() {
        return SOFTWARE_ID;
    }

}
