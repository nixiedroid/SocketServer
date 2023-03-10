package com.nixiedroid;

import com.nixiedroid.confg.ConfigStub;
import com.nixiedroid.settings.ServerSettingsStub;


public class Starter {

    public static void main(String[] args) {
        ConfigStub config = new ConfigStub(new SecretConfig());
        ServerSettingsStub settings = new ServerSettingsStub(new PcSettings());
        Program.setConfig(config,settings);
        Program.start();
    }



}
