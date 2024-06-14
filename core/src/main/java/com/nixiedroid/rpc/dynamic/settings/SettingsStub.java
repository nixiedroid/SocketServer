package com.nixiedroid.rpc.dynamic.settings;
import com.nixiedroid.rpc.dynamic.logger.LogLevel;


public abstract class SettingsStub {


    public abstract LogLevel getLevel();
    public abstract int getServerPort();
    public abstract int getPingTime();
    public abstract  int getDelayTime();
    public abstract int getMinClientCount();
    public abstract int getLang();
    public abstract String getHardwareID(); //Device id
    public abstract String getSoftwareID(); //Server software id

    public abstract void setLevel(LogLevel level);
    public abstract void setPort(int port);
    public abstract  void setPingTime(int pingTime) ;
    public abstract void setDelayTime(int delayTime);
    public abstract void setClientCount(int clientCount);
    public abstract void setHardwareID(String id);
    public abstract  void setLangCode(int langCode) ;
    public abstract  void setSoftwareId(String id);
}
