package com.nixiedroid.settings;

public class ServerSettingsStub implements ServerSettings {
    private final ServerSettings serverSettings;
    public ServerSettingsStub(ServerSettings serverSettings) {
        this.serverSettings = serverSettings;
    }
    @Override
    public LogLevel getLevel() {
        return serverSettings.getLevel();
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
    public int getDelayTime() {
        return serverSettings.getDelayTime();
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
    public int getLang() {
        return serverSettings.getLang();
    }
    @Override
    public String getSoftwareID() {
        return serverSettings.getSoftwareID();
    }

}
