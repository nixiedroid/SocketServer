package com.nixiedroid.settings;

public interface ServerSettings {
    LogLevel getLevel();
    int getServerPort();
    int getPingTime();
    int getDelayTime();
    int getMinClientCount();
    int getLang();
    String getHardwareID(); //Device id
    String getSoftwareID(); //Server software id

}
