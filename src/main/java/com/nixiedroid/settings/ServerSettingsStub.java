package com.nixiedroid.settings;

import com.nixiedroid.logger.LoggerStub;
import com.nixiedroid.sowftwareId.GeneratorStub;

public class ServerSettingsStub implements ServerSettings {
    private final ServerSettings serverSettings;

    public ServerSettingsStub(ServerSettings serverSettings) {
        this.serverSettings = serverSettings;
    }

    @Override
    public GeneratorStub getGenerator() {
        return serverSettings.getGenerator();
    }

    @Override
    public LoggerStub logger() {
        return serverSettings.logger();
    }

    @Override
    public LogLevel getLevel() {
        return serverSettings.getLevel();
    }

    @Override
    public void setLevel(LogLevel level) {
        serverSettings.setLevel(level);
    }

    @Override
    public int getServerPort() {
        return serverSettings.getServerPort();
    }

    @Override
    public int getPingTime() {
        return serverSettings.getPingTime();
    }

    @Override
    public void setPingTime(int pingTime) {
        serverSettings.setPingTime(pingTime);
    }

    @Override
    public int getDelayTime() {
        return serverSettings.getDelayTime();
    }

    @Override
    public void setDelayTime(int delayTime) {
        serverSettings.setDelayTime(delayTime);
    }

    @Override
    public int getMinClientCount() {
        return serverSettings.getMinClientCount();
    }

    @Override
    public String getHardwareID() {
        return serverSettings.getHardwareID();
    }

    @Override
    public void setHardwareID(String id) {
        serverSettings.setHardwareID(id);
    }

    @Override
    public int getLang() {
        return serverSettings.getLang();
    }

    @Override
    public String getSoftwareID() {
        return serverSettings.getSoftwareID();
    }

    @Override
    public void setPort(int port) {
        serverSettings.setPort(port);
    }

    @Override
    public void setClientCount(int clientCount) {
        serverSettings.setClientCount(clientCount);
    }

    @Override
    public void setLangCode(int langCode) {
        serverSettings.setLangCode(langCode);
    }

    @Override
    public void setSoftwareId(String id) {
        serverSettings.setSoftwareId(id);
    }


}
