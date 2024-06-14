package com.nixiedroid.rpc.dynamic.settings;

import com.nixiedroid.rpc.dynamic.logger.LogLevel;


public class SettingsExample extends SettingsStub {
    private String SOFTWARE_ID = null;

    private int PORT = 8080;
    private LogLevel LOGLEVEL = LogLevel.DEBUG;
    private int PING_TIME = 0x42; //MINUTES
    private int DELAY_TIME = 0x4242; //MINUTES
    private int CLIENT_COUNT = 25;
    private String HARDWARE_ID = "BAAAAAAAAAAAAAAD";
    private int LANG_CODE = 1033;

    @Override
    public LogLevel getLevel() {
        return LOGLEVEL;
    }

    @Override
    public void setLevel(LogLevel level) {
        this.LOGLEVEL = level;
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
    public void setPingTime(int pingTime) {
        this.PING_TIME = pingTime;
    }

    @Override
    public int getDelayTime() {
        return DELAY_TIME;
    }

    @Override
    public void setDelayTime(int delayTime) {
        this.DELAY_TIME = delayTime;
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
    public void setHardwareID(String id) {
        this.HARDWARE_ID = id;
    }

    @Override
    public int getLang() {
        return LANG_CODE;
    }

    @Override
    public String getSoftwareID() {
        return SOFTWARE_ID;
    }

    @Override
    public void setPort(int port) {
        this.PORT = port;
    }

    @Override
    public void setClientCount(int clientCount) {
        this.CLIENT_COUNT = clientCount;
    }

    @Override
    public void setLangCode(int langCode) {
        this.LANG_CODE = langCode;
    }

    @Override
    public void setSoftwareId(String id) {
        this.SOFTWARE_ID = id;
    }

}
