package com.nixiedroid.settings;

import com.nixiedroid.PcSettings;
import com.nixiedroid.logger.LoggerStub;
import com.nixiedroid.sowftwareId.GeneratorStub;

public interface ServerSettings {
    GeneratorStub getGenerator();
    LoggerStub logger();
    LogLevel getLevel();
    int getServerPort();
    int getPingTime();
    int getDelayTime();
    int getMinClientCount();
    int getLang();
    String getHardwareID(); //Device id
    String getSoftwareID(); //Server software id

    void setLevel(LogLevel level);
    void setPort(int port);
    void setPingTime(int pingTime) ;
    void setDelayTime(int delayTime);
    void setClientCount(int clientCount);
    void setHardwareID(String id);
    void setLangCode(int langCode) ;
    void setSoftwareId(String id);
}
